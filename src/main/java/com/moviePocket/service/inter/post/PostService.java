/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.post;

import com.moviePocket.controller.dto.post.PostDTO;

import java.util.List;

public interface PostService {

    PostDTO createPostList(String title, String content, Long idList);

    PostDTO createPostMovie(String title, String content, Long idMovie);

    PostDTO createPostPerson(String title, String content, Long idPerson);

    PostDTO updatePost(Long idPost, String title, String content);

    void deletePost(Long idPost);

    List<PostDTO> getAllByIdMovie(Long idMovie);

    List<PostDTO> getAllByIdPerson(Long idPerson);

    List<PostDTO> getAllByUser();

    List<PostDTO> getAllByIdList(Long idList);

    List<PostDTO> findAllByTitle(String title);

    PostDTO getPost(Long idPost);

    List<PostDTO> getAllByUsernamePosts(String username);

    List<PostDTO> getNewestPosts();

    List<PostDTO> getOldestPosts();

    boolean authorshipCheck(Long idPost);

    List<PostDTO> getTop10LatestPosts();

    List<PostDTO> getTop10LikedPosts();

    List<PostDTO> getAllByPartialTitle(String title);


}
