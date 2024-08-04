/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.list;

import com.moviePocket.controller.dto.review.ReactionDTO;
import com.moviePocket.db.entities.list.ReactionList;
import com.moviePocket.db.repository.list.LikeListRepository;
import com.moviePocket.db.repository.list.MovieListRepository;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.inter.list.ListReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ListReactionServiceImpl implements ListReactionService {

    private final LikeListRepository likeListRepository;
    private final MovieListRepository movieListRepository;
    private final AuthUser auth;

    @Override
    public void setLikeOrDisLike(Long idList, boolean reaction) {
        var user = auth.getAuthenticatedUser();
        var movieList = movieListRepository.findById(idList)
                .orElseThrow(() -> new NotFoundException("Movie list not found with id " + idList));
        var existingLike = likeListRepository.findByUserAndMovieList(user, movieList);

        if (existingLike.isPresent()) {
            var likeList = existingLike.get();
            if (likeList.isReaction() != reaction) {
                likeList.setReaction(reaction);
                likeListRepository.save(likeList);
            }
        } else {
            likeListRepository.save(new ReactionList(movieList, user, reaction));
        }
    }

    @Override
    public void deleteReaction(Long idList) {
        var user = auth.getAuthenticatedUser();
        var movieList = movieListRepository.findById(idList)
                .orElseThrow(() -> new NotFoundException("Movie list not found with id " + idList));
        var existingLike = likeListRepository.findByUserAndMovieList(user, movieList)
                .orElseThrow(() -> new NotFoundException("Reaction not found for movie list id " + idList));
        likeListRepository.delete(existingLike);
    }

    @Override
    public Boolean getReaction(Long idList) {
        var user = auth.getAuthenticatedUser();
        var movieList = movieListRepository.findById(idList)
                .orElseThrow(() -> new NotFoundException("Movie list not found with id " + idList));
        var existingLike = likeListRepository.findByUserAndMovieList(user, movieList);
        if (existingLike.isEmpty()) {
            return null;
        }
        return existingLike.get().isReaction();
    }

    @Override
    public ReactionDTO getAllReactionReview(Long idList) {
        var movieList = movieListRepository.findById(idList)
                .orElseThrow(() -> new NotFoundException("Movie list not found with id " + idList));
        return ReactionDTO.builder()
                .likes(likeListRepository.countByMovieReviewAndLickOrDisIsTrue(movieList))
                .dislikes(likeListRepository.countByMovieReviewAndLickOrDisIsFalse(movieList))
                .build();
    }
}
