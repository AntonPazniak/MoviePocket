/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.repository.rating;

import com.moviePocket.entities.rating.FavoriteMovie;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie,Long> {

    FavoriteMovie findByUserAndMovie_id(User user, Long idMovie);

    @Query("SELECT u FROM FavoriteMovie u WHERE u.user = :user ORDER BY u.created ASC")
    List<FavoriteMovie> findAllByUserOrderByCreatedAsc(@Param("user") User user);

    @Query("SELECT COUNT(u) FROM FavoriteMovie u WHERE u.movie.id = :movieId")
    int getAllCountByIdMovie(@Param("movieId") Long idMovie);


}