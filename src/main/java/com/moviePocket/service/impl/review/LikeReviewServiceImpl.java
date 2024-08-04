/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.review;


import com.moviePocket.controller.dto.review.ReactionDTO;
import com.moviePocket.db.entities.review.ReviewReaction;
import com.moviePocket.db.repository.review.LikeReviewRepository;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.inter.raview.LikeReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class LikeReviewServiceImpl implements LikeReviewService {

    private final LikeReviewRepository likeReviewRepository;
    private final AuthUser auth;
    private final ReviewServiceImpl reviewService;

    @Override
    public void setLikeOrDisLike(Long idReview, boolean reaction) {
        var user = auth.getAuthenticatedUser();
        var review = reviewService.getReviewByIdOrThrow(idReview);
        var existingLike = likeReviewRepository.findByUserAndReview(user, review);

        if (existingLike.isPresent()) {
            var like = existingLike.get();
            if (like.isReaction() != reaction) {
                like.setReaction(reaction);
                likeReviewRepository.save(like);
            }
        } else {
            likeReviewRepository.save(ReviewReaction.builder()
                    .reaction(reaction)
                    .review(review)
                    .user(user)
                    .build());
        }
    }

    @Override
    public void deleteReaction(Long idReview) {
        var user = auth.getAuthenticatedUser();
        var review = reviewService.getReviewByIdOrThrow(idReview);
        var existingLike = likeReviewRepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new NotFoundException("Reaction not found on id review " + idReview));
        likeReviewRepository.delete(existingLike);
    }

    @Override
    public Boolean getReaction(Long idReview) {
        var user = auth.getAuthenticatedUser();
        var review = reviewService.getReviewByIdOrThrow(idReview);
        var existingLike = likeReviewRepository.findByUserAndReview(user, review);
        if (existingLike.isEmpty()) {
            return null;
        }
        return existingLike.get().isReaction();
    }


    @Override
    public ReactionDTO getAllReactionReview(Long idReview) {
        return null;
    }
}
