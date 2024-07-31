/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.reaction;

import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.rating.WatchedMovie;
import com.moviePocket.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, Long> {

    Optional<WatchedMovie> findByUserAndMovie_Id(User user, Long idMovie);

    Boolean existsByUserAndMovie_Id(User user, Long idMovie);

    @Query("SELECT u.movie FROM WatchedMovie u WHERE u.user = :user ORDER BY u.created ASC")
    List<Movie> findAllMoviesByUser(@Param("user") User user);

    @Query("SELECT COUNT(u) FROM WatchedMovie u WHERE u.movie.id = :movieId")
    Integer getAllCountByMovieId(@Param("movieId") Long idMovie);

    @Query("SELECT COUNT(wm) FROM WatchedMovie wm WHERE wm.user = :user AND wm.movie.id IN (SELECT m.id FROM ListMovie lm JOIN lm.movies m WHERE lm.id = :listMovieId)")
    Integer getCountWatchedFromList(@Param("user") User user, @Param("listMovieId") Long listMovieId);
}
