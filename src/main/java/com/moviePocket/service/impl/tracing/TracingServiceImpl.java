/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.tracing;

import com.moviePocket.api.TMDBApi;
import com.moviePocket.api.models.MovieTMDB;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.tracking.Tracking;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.tracking.TrackingRepository;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.user.EmailSenderService;
import com.moviePocket.service.inter.tracing.TracingService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.moviePocket.util.BuildMailReleased.buildEmailReleased;

@Service
@AllArgsConstructor
@Slf4j
public class TracingServiceImpl implements TracingService {

    private final TrackingRepository trackingRepository;
    private final MovieServiceImpl movieService;
    private final EmailSenderService emailSenderService;
    private final AuthUser auth;


    @Override
    public Boolean getByIdMovie(Long idMovie) {
        User user = auth.getAuthenticatedUser();
        return trackingRepository.existsByUserAndMovie_Id(user, idMovie);
    }

    @Override
    public void setOrDel(Long idMovie) {
        User user = auth.getAuthenticatedUser();
        var tracking = trackingRepository.findByUserAndMovie_Id(user, idMovie);
        if (tracking.isEmpty()) {
            Movie movie = movieService.setMovieIfNotExist(idMovie);
            LocalDate currentDate = LocalDate.now();
            LocalDate releaseDate = movie.getRelease_date();
            if (releaseDate.isAfter(currentDate)) {
                trackingRepository.save(
                        Tracking.builder()
                                .movie(movie)
                                .user(user)
                                .dateRelease(movie.getRelease_date())
                                .build()
                );
            }
        } else {
            trackingRepository.delete(tracking.get());
        }
    }

    @Override
    public List<Movie> getAllByUser() {
        User user = auth.getAuthenticatedUser();
        var trackingList = trackingRepository.findAllByUser(user);
        return trackingList.stream().map(Tracking::getMovie).toList();
    }

    @Override
    public Long getCountByIdMovie(Long idMovie) {
        return trackingRepository.countAllByMovie_id(idMovie);
    }

    @Scheduled(cron = "0 00 20 * * *", zone = "Europe/Warsaw") // every day at 21:20 PL
    public void sendDailyMessages() throws MessagingException {

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        var trackingList = trackingRepository.findByDateRelease(tomorrow);

        trackingList.forEach(
                tracking -> {
                    String username = tracking.getUser().getLogin();
                    MovieTMDB movie = TMDBApi.getInfoMovie(tracking.getMovie().getId());
                    String link = "https://moviepocket.projektstudencki.pl/film/" + tracking.getMovie().getId();
                    try {
                        emailSenderService.sendMailWithAttachment(tracking.getUser().getEmail(), buildEmailReleased(username, movie.getTitle(), movie.getOverview(), link)
                                , "Movie release tomorrow " + movie.getTitle());
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        trackingRepository.deleteAll(trackingList);

    }

}
