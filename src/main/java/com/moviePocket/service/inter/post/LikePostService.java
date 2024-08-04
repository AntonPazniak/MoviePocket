/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.post;

import com.moviePocket.controller.dto.review.ReactionDTO;

public interface LikePostService {


    void setLikeOrDis(Long id, boolean likeOrDis);

    Boolean getReaction(Long id);

    ReactionDTO getAllLikeAndDisByIdPost(Long idPost);

    void deleteReaction(Long idPost);
}
