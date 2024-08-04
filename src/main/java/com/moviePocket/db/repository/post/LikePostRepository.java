/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.post;

import com.moviePocket.db.entities.post.Post;
import com.moviePocket.db.entities.post.ReactionPost;
import com.moviePocket.db.entities.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface LikePostRepository extends JpaRepository<ReactionPost, Long> {

    Optional<ReactionPost> getByUserAndPost_Id(User user, Long idPost);

    @Query("SELECT COUNT(lmr) FROM ReactionPost lmr WHERE lmr.post.id = :postId AND lmr.reaction = true")
    int countByPostAndLikeIsTrue(@Param("postId") Long postId);

    @Query("SELECT COUNT(lmr) FROM ReactionPost lmr WHERE lmr.post.id = :postId AND lmr.reaction = false")
    int countByPostAndLikeIsFalse(@Param("postId") Long postId);

    void deleteAllByPost(Post post);

    boolean existsByUserAndPost(User user, Post post);
}
