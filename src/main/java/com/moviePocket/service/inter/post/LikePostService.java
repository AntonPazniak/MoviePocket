/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.post;

import com.moviePocket.db.entities.post.PostDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LikePostService {
    ResponseEntity<Void> setLikeOrDisOrDel(String username, Long id, boolean likeOrDis);

    ResponseEntity<Boolean> getLikeOrDis(String username, Long id);

    ResponseEntity<Integer[]> getAllLikeAndDisByIdPost(Long idPost);

    ResponseEntity<List<PostDTO>> getMostLikedPosts();

    ResponseEntity<List<PostDTO>> getLeastLikedPosts();

}
