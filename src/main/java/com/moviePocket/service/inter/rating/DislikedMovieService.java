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

public interface DislikedMovieService {

    ResponseEntity<Void> setOrDeleteDislikedMovie(String email, Long idMovie);

    ResponseEntity<Boolean> getFromDislikedMovie(String email, Long idMovie);

    ResponseEntity<List<Movie>> getAllUserDislikedMovie(String email);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);

}