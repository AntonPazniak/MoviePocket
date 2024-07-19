/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.repository.rating;


import com.moviePocket.entities.rating.WatchedMovie;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, Long> {

    WatchedMovie findByUser_EmailAndMovie_Id(String email, Long idMovie);

    boolean existsByUser_EmailAndMovie_Id(String email, Long idMovie);

    @Query("SELECT u FROM WatchedMovie u WHERE u.user.email = :email ORDER BY u.created ASC")
    List<WatchedMovie> findAllByUserOrderByCreatedAsc(@Param("email") String email);

    @Query("SELECT COUNT(u) FROM WatchedMovie u WHERE u.movie.id = :movieId")
    int getAllCountByIdMovie(@Param("movieId") Long idMovie);

    @Query("SELECT COUNT(wm) FROM WatchedMovie wm WHERE wm.user = :user AND wm.movie.id IN (SELECT m.id FROM ListMovie lm JOIN lm.movies m WHERE lm.id = :listMovieId)")
    int getCountWatchedFromList(@Param("user") User user, @Param("listMovieId") Long listMovieId);
}
