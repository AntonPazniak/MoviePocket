/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.rating;

import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.rating.FavoriteMovie;
import com.moviePocket.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {

    Optional<FavoriteMovie> findByUserAndMovie_Id(User user, Long idMovie);

    Boolean existsByUserAndMovie_Id(User user, Long idMovie);

    @Query("SELECT u.movie FROM FavoriteMovie u WHERE u.user = :user")
    List<Movie> findAllMoviesByUser(@Param("user") User user);

    @Query("SELECT COUNT(u) FROM FavoriteMovie u WHERE u.movie.id = :movieId")
    Integer getAllCountByMovieId(@Param("movieId") Long movieId);
}
