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

public interface PostService {
    ResponseEntity<PostDTO> createPostList(String email, String title, String content, Long idList);

    ResponseEntity<PostDTO> createPostMovie(String email, String title, String content, Long idMovie);

    ResponseEntity<PostDTO> createPostPerson(String email, String title, String content, Long idPerson);

    ResponseEntity<Void> updatePost(String email, Long idPost, String title, String content);

    ResponseEntity<Void> deletePost(String email, Long idPost);

    ResponseEntity<List<PostDTO>> getAllByIdMovie(Long idMovie);

    ResponseEntity<List<PostDTO>> getAllByIdPerson(Long idPerson);

    ResponseEntity<List<PostDTO>> getAllByUser(String email);

    ResponseEntity<List<PostDTO>> getAllByIdList(Long idList);

    ResponseEntity<List<PostDTO>> getAllByTitle(String title);

    ResponseEntity<PostDTO> getPost(Long idPost);

    ResponseEntity<List<PostDTO>> getAllMyPosts(String email);

    ResponseEntity<List<PostDTO>> getAllByUsernamePosts(String username);

    ResponseEntity<List<PostDTO>> getNewestPosts();

    ResponseEntity<List<PostDTO>> getOldestPosts();

    ResponseEntity<Boolean> authorshipCheck(Long idPost, String username);

    ResponseEntity<List<PostDTO>> getTop10LatestPosts();

    ResponseEntity<List<PostDTO>> getTop10LikedPosts();

    ResponseEntity<List<PostDTO>> getAllByPartialTitle(String title);


}
