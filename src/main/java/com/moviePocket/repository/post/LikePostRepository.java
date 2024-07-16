/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.repository.post;

import com.moviePocket.entities.post.LikePost;
import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    LikePost getByUserAndPost(User user, Post post);

    @Query("SELECT COUNT(lmr) FROM LikePost lmr WHERE lmr.post = :post AND lmr.lickOrDis = true")
    int countByPostAndLickOrDisIsTrue(@Param("post") Post post);

    @Query("SELECT COUNT(lmr) FROM LikePost lmr WHERE lmr.post = :post AND lmr.lickOrDis = false")
    int countByPostAndLickOrDisIsFalse(@Param("post") Post post);

    void deleteAllByPost(Post post);
}
