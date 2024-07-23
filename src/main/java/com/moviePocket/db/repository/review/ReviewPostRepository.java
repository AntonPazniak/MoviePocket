/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.review;

import com.moviePocket.db.entities.post.Post;
import com.moviePocket.db.entities.review.Review;
import com.moviePocket.db.entities.review.ReviewPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {

    ReviewPost findByReview(Review review);

    int countByPost_Id(Long idPost);

    @Query("SELECT rl.review FROM ReviewPost rl WHERE rl.post = :post")
    List<Review> findReviewsByPost(@Param("post") Post post);


}