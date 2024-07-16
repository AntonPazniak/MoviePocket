/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.repository.list;

import com.moviePocket.entities.list.ListGenres;
import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.movie.Genre;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface ListGenreRepository extends JpaRepository<ListGenres, Long> {

    ListGenres getByMovieListAndGenre(ListMovie movieList, Genre genre);

    void deleteAllByMovieList(ListMovie movieList);

    List<ListGenres> getAllByMovieList(ListMovie movieList);

}
