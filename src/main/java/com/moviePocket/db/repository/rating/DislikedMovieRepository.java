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
import com.moviePocket.db.entities.rating.DislikedMovie;
import com.moviePocket.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DislikedMovieRepository extends JpaRepository<DislikedMovie,Long> {

    Optional<DislikedMovie> getByUserAndMovie_Id(User user, Long idMovie);

    Boolean existsByUserAndMovie_Id(User user, Long idMovie);

    @Query("SELECT movie from DislikedMovie u WHERE u.user = :user")
    List<Movie> findAllByUser_Id(@Param("user") User user);

    @Query("SELECT COUNT(u) FROM DislikedMovie u WHERE u.movie.id = :movieId")
    Integer getAllCountByIdMovie(@Param("movieId") Long movieId);
}