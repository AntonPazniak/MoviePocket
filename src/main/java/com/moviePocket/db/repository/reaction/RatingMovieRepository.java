/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.reaction;

import com.moviePocket.db.entities.rating.RatingMovie;
import com.moviePocket.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingMovieRepository extends JpaRepository<RatingMovie,Long> {

    Optional<RatingMovie> findByUserAndMovie_id(User user, Long idMovie);

    List<RatingMovie> findAllByUser(User user);

    @Query("SELECT AVG(rm.rating) FROM RatingMovie rm WHERE rm.movie.id = :movieId")
    Optional<Float> getAverageRatingByMovieId(@Param("movieId") Long movieId);

    int countAllByMovie_id(Long idMovie);

}
