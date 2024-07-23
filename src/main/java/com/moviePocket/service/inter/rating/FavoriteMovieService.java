/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.rating;

import com.moviePocket.db.entities.movie.Movie;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FavoriteMovieService {

    ResponseEntity<Void> setOrDeleteNewFavoriteMovies(String email, Long idMovie);

    ResponseEntity<Boolean> getFromFavoriteMovies(String email, Long idMovie);

    public ResponseEntity<List<Movie>> getAllUserFavoriteMovies(String email);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);
}
