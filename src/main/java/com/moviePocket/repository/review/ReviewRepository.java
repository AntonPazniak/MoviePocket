/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.repository.review;

import com.moviePocket.entities.review.Review;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review getById(Long id);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user = :user")
    int countReviewsByUser(@Param("user") User user);

    List<Review> findAllByUser(User user);
}
