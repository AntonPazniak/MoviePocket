/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.review;

import com.moviePocket.db.entities.review.Review;
import com.moviePocket.db.entities.review.ReviewMovie;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface ReviewMovieRepository extends JpaRepository<ReviewMovie, Long> {


    @Query("SELECT rm.review FROM ReviewMovie rm WHERE rm.movie.id = :idMovie")
    List<Review> findReviewsByMovieId(@Param("idMovie") Long idMovie);

    int countByMovie_id(Long idMovie);

    Boolean existsByReview(Review review);

    ReviewMovie findByReview(Review review);
}
