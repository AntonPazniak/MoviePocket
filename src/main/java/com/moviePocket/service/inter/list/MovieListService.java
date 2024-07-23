/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.list;

import com.moviePocket.db.entities.list.ParsList;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MovieListService {

    ResponseEntity<ParsList> setList(String email, String title, String content);

    ResponseEntity<Void> updateList(String email, Long idMovieList, String title, String content);

    ResponseEntity<Void> deleteList(String email, Long idMovieList);

    ResponseEntity<List<ParsList>> getAllByPartialTitle(String title);

    ResponseEntity<ParsList> getList(Long idMovieList);

    ResponseEntity<List<ParsList>> getAllMyList(String email);

    ResponseEntity<List<ParsList>> getAllByUsernameList(String username);

    ResponseEntity<List<ParsList>> getAllListsContainingMovie(Long idMovie);

    ResponseEntity<Void> addOrDelItemLIst(String email, Long idList, Long idMovie);

    ResponseEntity<Boolean> authorshipCheck(Long idList, String username);

    ResponseEntity<Boolean> isMovieInList(Long idMovieList, Long idMovie);

    ResponseEntity<List<ParsList>> getTop10LatestLists();

    ResponseEntity<List<ParsList>> getTop10LikedLists();

}
