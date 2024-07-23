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

import java.util.List;

public interface WatchedMovieService {

    void setOrDeleteNewWatched(String email, Long idMovie);

    boolean getFromWatched(String email, Long idMovie);

    List<Movie> getAllUserWatched(String email);

    Integer getAllCountByIdMovie(Long idMovie);


}
