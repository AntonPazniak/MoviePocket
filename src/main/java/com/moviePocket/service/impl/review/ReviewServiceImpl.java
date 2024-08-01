/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.review;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.post.Post;
import com.moviePocket.db.entities.review.*;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.review.*;
import com.moviePocket.exception.ForbiddenException;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.list.MovieListServiceImpl;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.post.PostServiceImpl;
import com.moviePocket.service.inter.raview.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieServiceImpl movieService;
    private final AuthUser auth;
    private final LikeReviewRepository likeReviewRepository;
    private final ReviewMovieRepository reviewMovieRepository;
    private final MovieListServiceImpl listService;
    private final ReviewListRepository reviewListRepository;
    private final PostServiceImpl postService;
    private final ReviewPostRepository reviewPostRepository;


    private Review createReview(String title, String content) {
        User user = auth.getAuthenticatedUser();
        Review review = new Review(user, title, content);
        reviewRepository.save(review);
        return review;
    }

    @Override
    public void createReviewMovie(Long idMovie, String title, String content) {
        Movie movie = movieService.setMovieIfNotExist(idMovie);
        Review review = createReview(title, content);
        ReviewMovie reviewMovie = new ReviewMovie(movie, review);
        reviewMovieRepository.save(reviewMovie);
    }

    @Override
    public void createReviewList(Long idList, String title, String content) {
        ListMovie movieList = listService.getListById(idList);
        Review review = createReview(title, content);
        ReviewList reviewList = new ReviewList(movieList, review);
        reviewListRepository.save(reviewList);
    }

    @Override
    public void createReviewPost(Long idPost, String title, String content) {
        var post = postService.getPostById(idPost);
        Review review = createReview(title, content);
        ReviewPost reviewPost = new ReviewPost(post, review);
        reviewPostRepository.save(reviewPost);
    }

    @Override
    public void updateReview(Long idReview, String title, String content) {
        User user = auth.getAuthenticatedUser();
        Review movieReview = getReviewByIdOrThrow(idReview);
        if (movieReview.getUser().equals(user)) {
            movieReview.setTitle(title);
            movieReview.setContent(content);
            reviewRepository.save(movieReview);
        } else {
            throw new ForbiddenException("You cannot modify a review if it is not yours.");
        }
    }

    @Override
    public void deleteReview(Long idReview) {
        User user = auth.getAuthenticatedUser();
        Review review = getReviewByIdOrThrow(idReview);

        if (!review.getUser().equals(user))
            throw new ForbiddenException("You cannot delete a review if it is not yours.");

        ReviewMovie reviewMovie = reviewMovieRepository.findByReview(review);
        ReviewList reviewList = reviewListRepository.findByReview(review);
        ReviewPost reviewPost = reviewPostRepository.findByReview(review);

        if (reviewMovie != null) {
            reviewMovieRepository.delete(reviewMovie);
        } else if (reviewList != null) {
            reviewListRepository.delete(reviewList);
        } else if (reviewPost != null) {
            reviewPostRepository.delete(reviewPost);
        }
        likeReviewRepository.deleteAllByReview(review);
        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDTO> getAllReviewByIdMovie(Long idMovie) {
        var review = reviewMovieRepository.findReviewsByMovieId(idMovie);
        return review.stream().map(this::parsReview).toList();
    }

    @Override
    public List<ReviewDTO> getAllReviewByIdList(Long idList) {
        ListMovie movieList = listService.getListById(idList);
        var reviews = reviewListRepository.findReviewsByMovieList(movieList);
        return reviews.stream().map(this::parsReview).toList();
    }

    @Override
    public List<ReviewDTO> getAllReviewByIdPost(Long idPost) {
        Post post = postService.getPostById(idPost);
        var reviews = reviewPostRepository.findReviewsByPost(post);
        return reviews.stream().map(this::parsReview).toList();
    }

    @Override
    public List<ReviewDTO> getAllReviewsByUser() {
        User user = auth.getAuthenticatedUser();
        var reviews = reviewRepository.findAllByUser(user);
        return reviews.stream().map(this::parsReview).toList();
    }

    @Override
    public ReviewDTO getReviewById(Long idReview) {
        return parsReview(getReviewByIdOrThrow(idReview));
    }

    @Override
    public Integer getCountReviewByIdMovie(Long idMovie) {
        return reviewMovieRepository.countByMovie_id(idMovie);
    }

    @Override
    public Integer getCountReviewByIdList(Long idList) {
        return reviewListRepository.countByMovieList_Id(idList);
    }

    @Override
    public Integer getCountReviewByIdPost(Long idList) {
        return reviewPostRepository.countByPost_Id(idList);
    }

    @Override
    public Integer getCountReviewByUser() {
        User user = auth.getAuthenticatedUser();
        return reviewRepository.countReviewsByUser(user);
    }

    @Override
    public Boolean getAuthorship(Long idReview) {
        User user = auth.getAuthenticatedUser();
        Review review = getReviewByIdOrThrow(idReview);
        return review.getUser().equals(user);
    }

    private ReviewDTO parsReview(Review review) {
        return ReviewDTO.builder()
                .title(review.getTitle())
                .content(review.getContent())
                .user(new UserPostDto(review.getUser().getUsername(),
                        review.getUser().getAvatar() != null ? review.getUser().getAvatar().getId() : null))
                .dataCreated(review.getCreated())
                .dataUpdated(review.getUpdated())
                .id(review.getId())
                .likes(likeReviewRepository.countByMovieReviewAndLickOrDisIsTrue(review))
                .dislike(likeReviewRepository.countByMovieReviewAndLickOrDisIsFalse(review))
                .build();
    }

    public Review getReviewByIdOrThrow(long idReview) {
        return reviewRepository.findById(idReview)
                .orElseThrow(() -> new NotFoundException("Review not found"));
    }

}
