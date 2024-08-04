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
import com.moviePocket.controller.dto.review.ReactionDTO;
import com.moviePocket.controller.dto.review.ReviewDTO;
import com.moviePocket.db.entities.Module;
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.post.Post;
import com.moviePocket.db.entities.review.*;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.review.ReviewListRepository;
import com.moviePocket.db.repository.review.ReviewMovieRepository;
import com.moviePocket.db.repository.review.ReviewPostRepository;
import com.moviePocket.db.repository.review.ReviewRepository;
import com.moviePocket.exception.ForbiddenException;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.list.MovieListServiceImpl;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.post.PostServiceImpl;
import com.moviePocket.service.inter.raview.ReviewService;
import com.moviePocket.util.ModulesConstant;
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
    private final ReviewMovieRepository reviewMovieRepository;
    private final MovieListServiceImpl listService;
    private final ReviewListRepository reviewListRepository;
    private final PostServiceImpl postService;
    private final ReviewPostRepository reviewPostRepository;


    private Review createReview(String title, String content, Module module, Long idItem) {
        User user = auth.getAuthenticatedUser();
        return reviewRepository.save(Review.builder()
                .user(user)
                .title(title)
                .content(content)
                .module(module)
                .idItem(idItem)
                .reactions(List.of())
                .build());
    }

    @Override
    public ReviewDTO createReviewMovie(Long idMovie, String title, String content) {
        Movie movie = movieService.setMovieIfNotExist(idMovie);
        Review review = createReview(title, content, ModulesConstant.movie, idMovie);
        ReviewMovie reviewMovie = ReviewMovie.builder()
                .movie(movie)
                .review(review)
                .build();
        return parsReview(
                reviewMovieRepository.save(reviewMovie)
                        .getReview()
        );
    }

    @Override
    public ReviewDTO createReviewList(Long idList, String title, String content) {
        ListMovie movieList = listService.getListByIdOrThrow(idList);
        Review review = createReview(title, content, ModulesConstant.list, idList);
        ReviewList reviewList = ReviewList.builder()
                .movieList(movieList)
                .review(review)
                .build();
        return parsReview(
                reviewListRepository.save(reviewList)
                        .getReview()
        );
    }

    @Override
    public ReviewDTO createReviewPost(Long idPost, String title, String content) {
        var post = postService.getPostByIdOrThrow(idPost);
        Review review = createReview(title, content, ModulesConstant.post, idPost);
        ReviewPost reviewPost = ReviewPost.builder()
                .post(post)
                .review(review)
                .build();
        return parsReview(
                reviewPostRepository.save(reviewPost)
                        .getReview()
        );
    }

    @Override
    public ReviewDTO updateReview(Long idReview, String title, String content) {
        User user = auth.getAuthenticatedUser();
        Review movieReview = getReviewByIdOrThrow(idReview);
        if (movieReview.getUser().equals(user)) {
            movieReview.setTitle(title);
            movieReview.setContent(content);
            return parsReview(reviewRepository.save(movieReview));
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

        if (review.getModule().getId().equals(ModulesConstant.movie.getId())) {
            reviewMovieRepository.delete(reviewMovieRepository.findByReview(review));
        } else if (review.getModule().getId().equals(ModulesConstant.list.getId())) {
            reviewListRepository.delete(reviewListRepository.findByReview(review));
        } else if (review.getModule().getId().equals(ModulesConstant.post.getId())) {
            reviewPostRepository.delete(reviewPostRepository.findByReview(review));
        }
        reviewRepository.delete(review);
    }

    @Override
    public List<ReviewDTO> getAllReviewByIdMovie(Long idMovie) {
        var review = reviewMovieRepository.findReviewsByMovieId(idMovie);
        return review.stream().map(this::parsReview).toList();
    }

    @Override
    public List<ReviewDTO> getAllReviewByIdList(Long idList) {
        ListMovie movieList = listService.getListByIdOrThrow(idList);
        var reviews = reviewListRepository.findReviewsByMovieList(movieList);
        return reviews.stream().map(this::parsReview).toList();
    }

    @Override
    public List<ReviewDTO> getAllReviewByIdPost(Long idPost) {
        Post post = postService.getPostByIdOrThrow(idPost);
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
                .user(UserPostDto.builder()
                        .avatar(review.getUser().getAvatar() != null ? review.getUser().getAvatar().getId() : null)
                        .username(review.getUser().getLogin())
                        .build()
                )
                .dataCreated(review.getCreated())
                .dataUpdated(review.getUpdated())
                .id(review.getId())
                .reactions(ReactionDTO.builder()
                        .likes((int) review.getReactions().stream().filter(ReviewReaction::isReaction).count())
                        .dislikes((int) review.getReactions().stream().filter(reaction -> !reaction.isReaction()).count())
                        .build())
                .build();
    }

    public Review getReviewByIdOrThrow(long idReview) {
        return reviewRepository.findById(idReview)
                .orElseThrow(() -> new NotFoundException("Review not found"));
    }

}
