/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.rating;

import com.moviePocket.entities.movie.Movie;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WatchedMovieService {

    ResponseEntity<Void> setOrDeleteNewWatched(String email, Long idMovie);

    ResponseEntity<Boolean> getFromWatched(String email, Long idMovie);

    ResponseEntity<List<Movie>> getAllUserWatched(String email);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);

    ResponseEntity<Integer> getCountWatchedFromList(String email, Long idMovieList);

}
