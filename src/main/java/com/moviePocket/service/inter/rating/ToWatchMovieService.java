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

public interface ToWatchMovieService {

    ResponseEntity<Void> setOrDeleteToWatch(String email, Long idMovie);

    ResponseEntity<Boolean> getFromToWatch(String email, Long idMovie);

    ResponseEntity<List<Movie>> getAllUserToWatch(String email);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);

}
