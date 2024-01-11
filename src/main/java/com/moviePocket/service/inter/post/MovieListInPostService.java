/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.post;

import org.springframework.http.ResponseEntity;

public interface MovieListInPostService {
    ResponseEntity<Void> addOrDelMovieListFromPost(String email, Long idPost, Long idMovieList);
}
