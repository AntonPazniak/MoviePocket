/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.raview;

import com.moviePocket.db.entities.review.ReviewDTO;

import java.util.List;

public interface ReviewService {

    void createReviewMovie(Long idMovie, String title, String content);

    void createReviewList(Long idList, String title, String content);

    void createReviewPost(Long idPost, String title, String content);

    void updateReview(Long idReview, String title, String content);

    void deleteReview(Long idReview);

    List<ReviewDTO> getAllReviewByIdMovie(Long idMovie);

    List<ReviewDTO> getAllReviewByIdList(Long idList);

    List<ReviewDTO> getAllReviewByIdPost(Long idPost);

    List<ReviewDTO> getAllReviewsByUser();

    ReviewDTO getReviewById(Long idReview);

    Integer getCountReviewByIdMovie(Long idMovie);

    Integer getCountReviewByIdList(Long idList);

    Integer getCountReviewByIdPost(Long idList);

    Integer getCountReviewByUser();

    Boolean getAuthorship(Long idReview);

}
