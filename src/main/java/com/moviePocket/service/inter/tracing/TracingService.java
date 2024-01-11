/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.tracing;

import org.springframework.http.ResponseEntity;

public interface TracingService {


    ResponseEntity<Boolean> existByIdMovie(String email, Long idMovie);

    ResponseEntity<Boolean> setOrDelByIdMovie(String email, Long idMovie);

    ResponseEntity<Long[]> getAllByUser(String email);

    Long getCountByIdMovie(Long idMovie);


}
