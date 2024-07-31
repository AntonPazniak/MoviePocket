/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.reaction;

import com.moviePocket.db.entities.rating.Rating;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RatingMovieService {

    ResponseEntity<Void> setNewRatingMovie(String email, Long idMovie, int rating);

    ResponseEntity<Void> removeFromRatingMovie(String email, Long idMovie);

    ResponseEntity<Integer> getFromRatingMovie(String email, Long idMovie);

    ResponseEntity<List<Rating>> getAllUserRatingMovie(String email);

    ResponseEntity<Double> getMovieRating(Long idFilm);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);
}
