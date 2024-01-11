/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.list;

import com.moviePocket.entities.movie.Genre;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoriesMovieListService {

    ResponseEntity<Void> setOrDelCategoryList(String email, Long idList, Long idCategory);

    ResponseEntity<List<Genre>> getAll();

}
