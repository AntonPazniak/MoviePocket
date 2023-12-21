package com.moviePocket.service.impl.tracing;

import com.moviePocket.api.TMDBApi;
import com.moviePocket.api.models.MovieTMDB;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.tracking.Tracking;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.tracking.TrackingRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.user.EmailSenderService;
import com.moviePocket.service.inter.tracing.TracingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;

import static com.moviePocket.util.BuildMailReleased.buildEmailReleased;

@Service
@AllArgsConstructor
@Slf4j
public class TracingServiceImpl implements TracingService {

    @Autowired
    private TrackingRepository trackingRepository;
    @Autowired
    private MovieServiceImpl movieService;
    @Autowired
    private UserRepository userRepository;
    private final EmailSenderService emailSenderService;


    public ResponseEntity<Boolean> existByIdMovie(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Boolean exist = trackingRepository.existsByUserAndMovie_Id(user, idMovie);
            return ResponseEntity.ok(exist);        // return bool if exist true not false
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // no sign in
    }

    public ResponseEntity<Boolean> setOrDelByIdMovie(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Tracking tracking = trackingRepository.findByUserAndMovie_Id(user, idMovie);
            if (tracking == null) {
                Movie movie = movieService.setMovie(idMovie);
                if (movie != null) {
                    LocalDate currentDate = LocalDate.now();
                    LocalDate releaseDate = movie.getRelease_date();

                    if (releaseDate.isAfter(currentDate)) {
                        tracking = new Tracking(movie.getRelease_date(), movie, user);
                        trackingRepository.save(tracking);
                        return ResponseEntity.ok(true); // save tracking
                    } else {
                        return ResponseEntity.ok(false); // movie has released
                    }
                } else {
                    return ResponseEntity.ok(false);  // movie not found
                }
            } else {
                trackingRepository.delete(tracking);
                return ResponseEntity.ok(true); // delete tracking movie
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // no sign in
    }

    public Long getCountByIdMovie(Long idMovie) {
        return trackingRepository.countAllByMovie_id(idMovie);
    }

    @Scheduled(cron = "0 00 20 * * *", zone = "Europe/Warsaw") // Каждый день в 21:20 по польскому времени
    public void sendDailyMessages() throws MessagingException {
        System.out.println("Рассылка сообщений...");

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Tracking> trackings = trackingRepository.findByDateRelease(tomorrow);
        if (trackingRepository != null) {
            for (Tracking t : trackings) {
                String username = t.getUser().getUsername();
                MovieTMDB movie = TMDBApi.getInfoMovie(t.getMovie().getId());
                String link = "https://moviepocket.projektstudencki.pl/film/" + t.getMovie().getId();
                emailSenderService.sendMailWithAttachment(t.getUser().getEmail(), buildEmailReleased(username, movie.getTitle(), movie.getOverview(), link)
                        , "Movie release tomorrow " + movie.getTitle());
            }
            trackingRepository.deleteAll(trackings);
        }

    }

    public ResponseEntity<Long[]> getAllByUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            List<Tracking> trackings = trackingRepository.findAllByUser(user);
            if (trackings != null) {
                Long[] idsMovie = new Long[trackings.size()];
                int index = 0;
                for (Tracking t : trackings) {
                    idsMovie[index] = t.getMovie().getId();
                    index++;
                }
                return ResponseEntity.ok(idsMovie);
            } else
                return ResponseEntity.ok(new Long[]{});
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }



}
