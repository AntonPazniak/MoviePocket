package com.moviePocket.service.impl.movie.tracing;

import com.moviePocket.api.TMDBApi;
import com.moviePocket.api.models.Movie;
import com.moviePocket.entities.movie.tracking.Tracking;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.movie.tracking.TrackingRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.user.EmailSenderService;
import com.moviePocket.service.movie.tracing.TracingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TracingServiceImpl implements TracingService {

    @Autowired
    private TrackingRepository trackingRepository;
    @Autowired
    private UserRepository userRepository;
    private final EmailSenderService emailSenderService;


    public ResponseEntity<Boolean> existByIdMovie(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Boolean exist = trackingRepository.existsByUserAndIdMovie(user, idMovie);
            return ResponseEntity.ok(exist);        // return bool if exist true not false
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // no sign in
    }

    public ResponseEntity<Boolean> setOrDelByIdMovie(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Tracking tracking = trackingRepository.findByUserAndIdMovie(user, idMovie);
            if (tracking == null) {
                Movie movie = TMDBApi.getInfoMovie(idMovie);
                if (movie != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, 0);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Date date = calendar.getTime();
                    if (movie.getReleaseDate().compareTo(date) > 0) {
                        tracking = new Tracking(user, idMovie, movie.getReleaseDate());
                        trackingRepository.save(tracking);
                        return ResponseEntity.ok(true); // save tracking
                    } else
                        return ResponseEntity.ok(false); // movie has released
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
        return trackingRepository.countByIdMovie(idMovie);
    }

    @Scheduled(cron = "0 00 20 * * *", zone = "Europe/Warsaw") // Каждый день в 21:20 по польскому времени
    public void sendDailyMessages() throws MessagingException {
        System.out.println("Рассылка сообщений...");


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date tomorrowDate = calendar.getTime();

        List<Tracking> trackings = trackingRepository.findByDateRelease(tomorrowDate);
        if (trackingRepository != null) {
            for (Tracking t : trackings) {
                Movie movie = TMDBApi.getInfoMovie(t.getIdMovie());
                emailSenderService.sendMailWithAttachment(t.getUser().getEmail(), "\"" + movie.getTitle() + "\n" + movie.getOverview()
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
                    idsMovie[index] = t.getIdMovie();
                    index++;
                }
                return ResponseEntity.ok(idsMovie);
            } else
                return ResponseEntity.ok(new Long[]{});
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }



}