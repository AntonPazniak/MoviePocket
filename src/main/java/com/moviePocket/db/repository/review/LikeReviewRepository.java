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
import com.moviePocket.db.entities.review.ReviewLike;
import com.moviePocket.db.entities.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface LikeReviewRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByUserAndReview(User user, Review review);

    void deleteAllByReview(Review review);

    @Query("SELECT COUNT(lmr) FROM ReviewLike lmr WHERE lmr.review = :movieReview AND lmr.lickOrDis = true")
    int countByMovieReviewAndLickOrDisIsTrue(Review movieReview);

    @Query("SELECT COUNT(lmr) FROM ReviewLike lmr WHERE lmr.review = :movieReview AND lmr.lickOrDis = false")
    int countByMovieReviewAndLickOrDisIsFalse(Review movieReview);


}
