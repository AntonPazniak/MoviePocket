/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.rating;

import com.moviePocket.db.entities.rating.DislikedMovie;
import com.moviePocket.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DislikedMovieRepository extends JpaRepository<DislikedMovie,Long> {

    DislikedMovie findByUserAndMovie_Id(User user, Long idMovie);

    List<DislikedMovie> findAllByUser(User user);

    @Query("SELECT COUNT(u) FROM DislikedMovie u WHERE u.movie.id = :movieId")
    int getAllCountByIdMovie(@Param("movieId") Long idMovie);
}