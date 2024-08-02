/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.list;

import com.moviePocket.controller.dto.review.ReactionDTO;

public interface ListReactionService {

    void setLikeOrDisLike(Long idReview, boolean reaction);

    void deleteReaction(Long idReview);

    Boolean getReaction(Long id);

    ReactionDTO getAllReactionReview(Long idReview);
}
