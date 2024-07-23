/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.rating;

import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.rating.DislikedMovie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.rating.DislikedMovieRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.rating.DislikedMovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DislikedMovieServiceImpl implements DislikedMovieService {

    private final DislikedMovieRepository dislikedMovieRepository;
    private final UserRepository userRepository;
    private final MovieServiceImpl movieService;

    @Transactional
    public ResponseEntity<Void> setOrDeleteDislikedMovie(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        DislikedMovie dislikedMovie = dislikedMovieRepository.findByUserAndMovie_Id(user, idMovie);
        Movie movie = movieService.setMovieIfNotExist(idMovie);
        if (movie != null) {
            if (dislikedMovie == null) {
                dislikedMovieRepository.save(
                        new DislikedMovie(userRepository.findByEmail(email), movie));
            } else {
                dislikedMovieRepository.delete(dislikedMovie);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Boolean> getFromDislikedMovie(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        DislikedMovie dislikedMovie = dislikedMovieRepository.findByUserAndMovie_Id(
                user, idMovie);
        return ResponseEntity.ok(dislikedMovie != null);
    }

    public ResponseEntity<List<Movie>> getAllUserDislikedMovie(String email) {
        List<DislikedMovie> favoriteMoviesList = dislikedMovieRepository.findAllByUser(
                userRepository.findByEmail(email));
        List<Movie> movies = new ArrayList<>();
        for (DislikedMovie dislikedMovie : favoriteMoviesList) {
            movies.add(dislikedMovie.getMovie());
        }
        if (movies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(movies);
    }

    public ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie) {
        int count = dislikedMovieRepository.getAllCountByIdMovie(idMovie);
        return ResponseEntity.ok(count);
    }
}
