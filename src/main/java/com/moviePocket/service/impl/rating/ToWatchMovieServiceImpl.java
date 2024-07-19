/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.rating;

import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.rating.ToWatchMovie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.rating.ToWatchMovieRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.rating.ToWatchMovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToWatchMovieServiceImpl implements ToWatchMovieService {

    private final ToWatchMovieRepository toWatchMovieRepository;
    private final UserRepository userRepository;
    private final MovieServiceImpl movieService;

    @Transactional
    public ResponseEntity<Void> setOrDeleteToWatch(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        ToWatchMovie toWatchMovie = toWatchMovieRepository.findByUserAndMovie_Id(
                user, idMovie);
        Movie movie = movieService.setMovieIfNotExist(idMovie);
        if (movie != null) {
            if (toWatchMovie == null) {
                toWatchMovieRepository.save(new ToWatchMovie(userRepository.findByEmail(email), movie));
            } else {
                toWatchMovieRepository.delete(toWatchMovie);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Boolean> getFromToWatch(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        ToWatchMovie toWatchMovies = toWatchMovieRepository.findByUserAndMovie_Id(
                user, idMovie);
        return ResponseEntity.ok(toWatchMovies != null);
    }

    public ResponseEntity<List<Movie>> getAllUserToWatch(String email) {
        List<ToWatchMovie> toWatchList = toWatchMovieRepository.findAllByUserOrderByCreatedAsc(
                userRepository.findByEmail(email));
        List<Movie> movies = toWatchList.stream()
                .map(ToWatchMovie::getMovie)
                .collect(Collectors.toList());

        if (movies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(movies);
    }

    public ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie) {
        int count = toWatchMovieRepository.getAllCountByIdMovie(idMovie);
        return ResponseEntity.ok(count);
    }
}
