/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.tracing;

import com.moviePocket.db.entities.movie.Movie;

import java.util.List;

public interface TracingService {


    Boolean getByIdMovie(Long idMovie);

    void setOrDel(Long idMovie);

    List<Movie> getAllByUser();

    Long getCountByIdMovie(Long idMovie);


}
