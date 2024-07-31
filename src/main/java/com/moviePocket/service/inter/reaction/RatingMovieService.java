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

import java.util.List;

public interface RatingMovieService {

    void setRating(Long idMovie, int rating);

    void removeRating(Long idMovie);

    Integer getMyRatingByIdMovie(Long idMovie);

    List<Rating> getAllUserRating();

    Float getRatingByIdMovie(Long idMovie);

    Integer getRatingCountByIdMovie(Long idMovie);
}
