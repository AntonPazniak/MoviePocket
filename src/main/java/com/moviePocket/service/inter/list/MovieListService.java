/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.list;

import com.moviePocket.controller.dto.list.ListDTO;

import java.util.List;

public interface MovieListService {

    ListDTO setList(String title, String content);

    ListDTO updateList(Long idMovieList, String title, String content);

    void deleteList(Long idMovieList);

    List<ListDTO> getAllByPartialTitle(String title);

    ListDTO getList(Long idMovieList);

    List<ListDTO> getAllUserList();

    List<ListDTO> getAllByUsernameList(String username);

    List<ListDTO> getAllListsContainingMovie(Long idMovie);

    void addOrDelItemList(Long idList, Long idMovie);

    boolean authorshipCheck(Long idList);

    boolean isMovieInList(Long idMovieList, Long idMovie);

    List<ListDTO> getTop10LatestLists();

    List<ListDTO> getTop10LikedLists();

}
