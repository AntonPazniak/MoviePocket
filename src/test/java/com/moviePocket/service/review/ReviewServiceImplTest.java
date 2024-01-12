/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.review;


import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.review.ParsReview;
import com.moviePocket.entities.review.Review;
import com.moviePocket.entities.review.ReviewMovie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.list.MovieListRepository;
import com.moviePocket.repository.post.PostRepository;
import com.moviePocket.repository.review.*;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.review.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReviewServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private LikeReviewRepository likeReviewRepository;

    @Mock
    private ReviewMovieRepository reviewMovieRepository;

    @Mock
    private MovieListRepository movieListRepository;

    @Mock
    private ReviewListRepository reviewListRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ReviewPostRepository reviewPostRepository;

    @Mock
    private MovieServiceImpl movieService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void createMovieReview_Success() {
        String email = "test@example.com";
        Long idMovie = 1L;
        String title = "Test Title";
        String content = "Test Content";

        User user = new User();
        user.setEmail(email);

        Movie movie = new Movie();

        Review review = new Review(user, title, content);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(movieService.setMovie(idMovie)).thenReturn(movie);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMovieRepository.save(any(ReviewMovie.class))).thenReturn(new ReviewMovie());

        ResponseEntity<Void> response = reviewService.createMovieReview(email, idMovie, title, content);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reviewMovieRepository, times(1)).save(any(ReviewMovie.class));
    }

    @Test
    void createMovieReview_Unauthorized() {
        String email = "test@example.com";
        Long idMovie = 1L;
        String title = "Test Title";
        String content = "Test Content";

        when(userRepository.findByEmail(email)).thenReturn(null);

        ResponseEntity<Void> response = reviewService.createMovieReview(email, idMovie, title, content);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(reviewMovieRepository, never()).save(any(ReviewMovie.class));
    }


    @Test
    void updateReview_Success() {
        // Mock data
        Long idReview = 1L;
        String username = "test@example.com";
        String title = "Updated Title";
        String content = "Updated Content";

        User user = new User();
        user.setEmail(username);

        Review existingReview = new Review(user, "Old Title", "Old Content");

        when(userRepository.findByEmail(username)).thenReturn(user);
        when(reviewRepository.getById(idReview)).thenReturn(existingReview);
        when(reviewRepository.save(any(Review.class))).thenReturn(existingReview);

        // Perform the test
        ResponseEntity<Void> response = reviewService.updateReview(idReview, username, title, content);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(title, existingReview.getTitle());
        assertEquals(content, existingReview.getContent());
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void updateReview_Unauthorized() {
        Long idReview = 1L;
        String username = "test@example.com";
        String title = "Updated Title";
        String content = "Updated Content";

        when(userRepository.findByEmail(username)).thenReturn(null);

        ResponseEntity<Void> response = reviewService.updateReview(idReview, username, title, content);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(reviewRepository, never()).save(any(Review.class));
    }


    @Test
    void delReview_Success() {
        Long idReview = 1L;
        String username = "test@example.com";

        User user = new User();
        user.setEmail(username);

        Review review = new Review(user, "Test Title", "Test Content");

        ReviewMovie reviewMovie = new ReviewMovie(new Movie(), review);

        lenient().when(userRepository.findByEmail(username)).thenReturn(user);
        lenient().when(reviewRepository.getById(idReview)).thenReturn(review);
        lenient().when(reviewMovieRepository.findByReview(review)).thenReturn(reviewMovie);
        lenient().when(likeReviewRepository.countByMovieReviewAndLickOrDisIsTrue(review)).thenReturn(0);
        lenient().when(likeReviewRepository.countByMovieReviewAndLickOrDisIsFalse(review)).thenReturn(0);

        ResponseEntity<Void> response = reviewService.delReview(idReview, username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reviewMovieRepository, times(1)).delete(reviewMovie);
        verify(likeReviewRepository, times(1)).deleteAllByReview(review);
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void delReview_UnauthorizedUser() {
        Long idReview = 1L;
        String username = "unauthorizedUser";

        when(userRepository.findByEmail(username)).thenReturn(null);

        ResponseEntity<Void> response = reviewService.delReview(idReview, username);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void delReview_ReviewNotFound() {
        Long idReview = 1L;
        String username = "authorizedUser";

        when(userRepository.findByEmail(username)).thenReturn(new User());
        when(reviewRepository.getById(idReview)).thenReturn(null);

        ResponseEntity<Void> response = reviewService.delReview(idReview, username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

//    @Test
//    void delReview_ForbiddenUser() {
//        // Mock data
//        Long idReview = 1L;
//        String authorizedUsername = "authorizedUser";
//        String anotherUsername = "anotherUser";
//
//        User authorizedUser = new User();
//        authorizedUser.setEmail(authorizedUsername);
//
//        User anotherUser = new User();
//        anotherUser.setEmail(anotherUsername);
//
//        Review review = new Review(anotherUser, "Title", "Content");
//
//        when(userRepository.findByEmail(authorizedUsername)).thenReturn(authorizedUser);
//        when(reviewRepository.getById(idReview)).thenReturn(review);
//
//        // Perform the test
//        ResponseEntity<Void> response = reviewService.delReview(idReview, authorizedUsername);
//
//        // Verify the results
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//    }

    @Test
    void getAllByIDMovie_Success() {
        Long idMovie = 1L;

        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(new User(), "Title1", "Content1"));
        reviews.add(new Review(new User(), "Title2", "Content2"));

        when(reviewMovieRepository.findReviewsByMovieId(idMovie)).thenReturn(reviews);

        ResponseEntity<List<ParsReview>> response = reviewService.getAllByIDMovie(idMovie);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews.size(), response.getBody().size());
    }

    @Test
    void getAllByIdList_Success() {
        // Mock data
        Long idList = 1L;

        ListMovie movieList = new ListMovie();
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(new User(), "Title1", "Content1"));
        reviews.add(new Review(new User(), "Title2", "Content2"));

        when(movieListRepository.getById(idList)).thenReturn(movieList);
        when(reviewListRepository.findReviewsByMovieList(movieList)).thenReturn(reviews);

        ResponseEntity<List<ParsReview>> response = reviewService.getAllByIdList(idList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews.size(), response.getBody().size());
    }

    @Test
    void getCountByIdList_Success() {
        Long idList = 1L;
        int expectedCount = 2;

        when(reviewListRepository.countByMovieList_Id(idList)).thenReturn(expectedCount);

        ResponseEntity<Integer> response = reviewService.getCountByIdList(idList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCount, response.getBody());
    }

    @Test
    void getAllByIdPost_Success() {
        Long idPost = 1L;

        Post post = new Post();
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(new User(), "Title1", "Content1"));
        reviews.add(new Review(new User(), "Title2", "Content2"));

        when(postRepository.getById(idPost)).thenReturn(post);
        when(reviewPostRepository.findReviewsByPost(post)).thenReturn(reviews);

        ResponseEntity<List<ParsReview>> response = reviewService.getAllByIdPost(idPost);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews.size(), response.getBody().size());
    }

    @Test
    void getCountByIdPost_Success() {
        Long idPost = 1L;
        int expectedCount = 2;

        when(reviewPostRepository.countByPost_Id(idPost)).thenReturn(expectedCount);

        ResponseEntity<Integer> response = reviewService.getCountByIdPost(idPost);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCount, response.getBody());
    }

    @Test
    void getAllReviewsByUser_Success() {
        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);

        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(user, "Title1", "Content1"));
        reviews.add(new Review(user, "Title2", "Content2"));

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(reviewRepository.findAllByUser(user)).thenReturn(reviews);

        ResponseEntity<List<ParsReview>> response = reviewService.getAllReviewsByUser(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews.size(), response.getBody().size());
    }

    @Test
    void getCountByUser_Success() {
        String email = "test@example.com";
        int expectedCount = 3;

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(reviewRepository.countReviewsByUser(user)).thenReturn(expectedCount);

        ResponseEntity<Integer> response = reviewService.getCountByUser(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedCount, response.getBody());
    }
}
