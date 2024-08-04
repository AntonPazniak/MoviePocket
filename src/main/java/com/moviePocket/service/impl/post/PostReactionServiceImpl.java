/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.post;

import com.moviePocket.controller.dto.review.ReactionDTO;
import com.moviePocket.db.entities.post.Post;
import com.moviePocket.db.entities.post.ReactionPost;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.post.LikePostRepository;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.inter.post.LikePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReactionServiceImpl implements LikePostService {

    private final LikePostRepository reactionRepository;
    private final PostServiceImpl postService;
    private final AuthUser auth;

    @Override
    public void setLikeOrDis(Long idPost, boolean likeOrDis) {
        User user = auth.getAuthenticatedUser();
        var likePost = reactionRepository.getByUserAndPost_Id(user, idPost);
        if (likePost.isPresent()) {
            likePost.get().setReaction(likeOrDis);
            reactionRepository.save(likePost.get());
        } else {
            Post post = postService.getPostByIdOrThrow(idPost);
            reactionRepository.save(
                    ReactionPost.builder()
                            .post(post)
                            .reaction(likeOrDis)
                            .user(user)
                            .build()
            );
        }
    }

    @Override
    public Boolean getReaction(Long idPost) {
        User user = auth.getAuthenticatedUser();
        return reactionRepository.getByUserAndPost_Id(user, idPost).isPresent();
    }

    @Override
    public void deleteReaction(Long idPost) {
        User user = auth.getAuthenticatedUser();
        var reaction = reactionRepository.getByUserAndPost_Id(user, idPost);
    }

    @Override
    public ReactionDTO getAllLikeAndDisByIdPost(Long idPost) {
        return ReactionDTO.builder()
                .likes(reactionRepository.countByPostAndLikeIsTrue(idPost))
                .dislikes(reactionRepository.countByPostAndLikeIsFalse(idPost))
                .build();
    }

}
