/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.review;

import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.review.Review;
import com.moviePocket.db.entities.review.ReviewList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewListRepository extends JpaRepository<ReviewList, Long> {
    ReviewList findByReview_Id(Long idReview);

    @Query("SELECT rl.review FROM ReviewList rl WHERE rl.movieList = :movieList")
    List<Review> findReviewsByMovieList(@Param("movieList") ListMovie movieList);

    int countByMovieList_Id(Long idList);

    ReviewList findByReview(Review review);

}