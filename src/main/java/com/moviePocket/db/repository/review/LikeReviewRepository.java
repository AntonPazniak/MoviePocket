/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.review;

import com.moviePocket.db.entities.review.LikeMovieReview;
import com.moviePocket.db.entities.review.Review;
import com.moviePocket.db.entities.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface LikeReviewRepository extends JpaRepository<LikeMovieReview, Long> {

    LikeMovieReview getByUserAndReview(User user, Review review);

    void deleteAllByReview(Review review);

    @Query("SELECT COUNT(lmr) FROM LikeMovieReview lmr WHERE lmr.review = :movieReview AND lmr.lickOrDis = true")
    int countByMovieReviewAndLickOrDisIsTrue(Review movieReview);

    @Query("SELECT COUNT(lmr) FROM LikeMovieReview lmr WHERE lmr.review = :movieReview AND lmr.lickOrDis = false")
    int countByMovieReviewAndLickOrDisIsFalse(Review movieReview);


}
