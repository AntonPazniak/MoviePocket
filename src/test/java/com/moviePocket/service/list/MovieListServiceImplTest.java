/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.list;

import com.moviePocket.controller.dto.review.ReviewDTO;
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.list.ParsList;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.review.Review;
import com.moviePocket.db.entities.review.ReviewMovie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.list.LikeListRepository;
import com.moviePocket.db.repository.list.ListGenreRepository;
import com.moviePocket.db.repository.list.MovieListRepository;
import com.moviePocket.db.repository.post.PostRepository;
import com.moviePocket.db.repository.review.*;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.service.impl.list.MovieListServiceImpl;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.review.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MovieListServiceImplTest {

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
    @InjectMocks
    private MovieListServiceImpl movieListService;
    @Mock
    private ListGenreRepository listGenreRepository;

    @Mock
    private LikeListRepository likeListRepository;

    @Test
    void testSetListSuccess() {
        // Mock data
        String email = "test@example.com";
        String title = "Test List";
        String content = "Test Content";

        // Mock user
        User user = new User();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock repository save
        ListMovie savedList = new ListMovie();
        Mockito.when(movieListRepository.save(Mockito.any(ListMovie.class))).thenReturn(savedList);

        // Mock countByMovieReviewAndLickOrDisIsTrue
        when(likeListRepository.countByMovieReviewAndLickOrDisIsTrue(Mockito.any(ListMovie.class))).thenReturn(0);

        // Mock countByMovieReviewAndLickOrDisIsFalse
        when(likeListRepository.countByMovieReviewAndLickOrDisIsFalse(Mockito.any(ListMovie.class))).thenReturn(0);


        // Test service method
        ResponseEntity<ParsList> response = movieListService.setList(email, title, content);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void setList_UnauthorizedUser() {
        // Mock data
        String email = "unauthorized@example.com";
        String title = "Test List";
        String content = "Test Content";

        when(userRepository.findByEmail(email)).thenReturn(null);

        // Perform the test
        ResponseEntity<ParsList> response = movieListService.setList(email, title, content);

        // Verify the results
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(movieListRepository, never()).save(any(ListMovie.class));
    }

    @Test
    void createMovieReview_Failure() {
        // Test the case where the movie is not found
        String email = "test@example.com";
        Long idMovie = 1L;
        String title = "Test Title";
        String content = "Test Content";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(movieService.setMovieIfNotExist(idMovie)).thenReturn(null);

        ResponseEntity<Void> response = reviewService.createMovieReview(email, idMovie, title, content);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reviewMovieRepository, never()).save(any(ReviewMovie.class));
    }

    @Test
    void updateReview_ReviewNotFound() {
        Long idReview = 1L;
        String username = "test@example.com";
        String title = "Updated Title";
        String content = "Updated Content";

        User user = new User();
        user.setEmail(username);

        when(userRepository.findByEmail(username)).thenReturn(user);
        when(reviewRepository.getById(idReview)).thenReturn(null);

        ResponseEntity<Void> response = reviewService.updateReview(idReview, username, title, content);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void delReview_Failure() {
        // Test the case where the review has associated likes
        Long idReview = 1L;
        String username = "test@example.com";

        User user = new User();
        user.setEmail(username);

        Review review = new Review(user, "Test Title", "Test Content");

        ReviewMovie reviewMovie = new ReviewMovie(new Movie(), review);

        when(userRepository.findByEmail(username)).thenReturn(user);
        when(reviewRepository.getById(idReview)).thenReturn(review);
        when(reviewMovieRepository.findByReview(review)).thenReturn(reviewMovie);
        when(likeReviewRepository.countByMovieReviewAndLickOrDisIsTrue(review)).thenReturn(1);

        ResponseEntity<Void> response = reviewService.delReview(idReview, username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllByIDMovie_NoReviews() {
        Long idMovie = 1L;

        when(reviewMovieRepository.findReviewsByMovieId(idMovie)).thenReturn(new ArrayList<>());

        ResponseEntity<List<ReviewDTO>> response = reviewService.getAllByIDMovie(idMovie);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void getAllByIdList_EmptyList() {
        Long idList = 1L;

        ListMovie movieList = new ListMovie();

        when(movieListRepository.getById(idList)).thenReturn(movieList);
        when(reviewListRepository.findReviewsByMovieList(movieList)).thenReturn(new ArrayList<>());

        ResponseEntity<List<ReviewDTO>> response = reviewService.getAllByIdList(idList);

        assertEquals(0, response.getBody().size());
    }

    @Test
    void testUpdateListSuccess() {
        // Mock data
        String email = "test@example.com";
        Long idMovieList = 1L;
        String title = "Updated Title";
        String content = "Updated Content";

        // Mock user
        User user = new User();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock repository methods
        ListMovie existingList = new ListMovie();
        existingList.setUser(user);
        Mockito.when(movieListRepository.getById(idMovieList)).thenReturn(existingList);
        Mockito.when(movieListRepository.save(Mockito.any(ListMovie.class))).thenReturn(existingList);

        // Test service method
        ResponseEntity<Void> response = movieListService.updateList(email, idMovieList, title, content);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteListSuccess() {
        // Mock data
        String email = "test@example.com";
        Long idMovieList = 1L;

        // Mock user
        User user = new User();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock repository methods
        ListMovie existingList = new ListMovie();
        existingList.setUser(user);
        Mockito.when(movieListRepository.getById(idMovieList)).thenReturn(existingList);
        Mockito.when(reviewListRepository.findReviewsByMovieList(existingList)).thenReturn(Collections.emptyList());

        // Test service method
        ResponseEntity<Void> response = movieListService.deleteList(email, idMovieList);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddOrDelItemListSuccess() {
        // Mock data
        String email = "test@example.com";
        Long idList = 1L;
        Long idMovie = 2L;

        // Mock user
        User user = new User();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock repository methods
        ListMovie existingList = new ListMovie();
        existingList.setUser(user);
        existingList.setMovies(new ArrayList<>());
        Mockito.when(movieListRepository.getById(idList)).thenReturn(existingList);
        Mockito.when(movieService.setMovieIfNotExist(idMovie)).thenReturn(new Movie());

        // Test service method
        ResponseEntity<Void> response = movieListService.addOrDelItemLIst(email, idList, idMovie);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testAddOrDelItemListUnauthorizedUser() {
        // Mock data
        String email = "test@example.com";
        Long idList = 1L;
        Long idMovie = 2L;

        // Mock user
        User user = new User();
        User anotherUser = new User();  // Different user
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock repository methods
        ListMovie existingList = new ListMovie();
        existingList.setUser(anotherUser);  // User is not the owner of the list
        existingList.setMovies(new ArrayList<>());
        Mockito.when(movieListRepository.getById(idList)).thenReturn(existingList);
        Mockito.when(movieService.setMovieIfNotExist(idMovie)).thenReturn(new Movie());

        // Test service method
        ResponseEntity<Void> response = movieListService.addOrDelItemLIst(email, idList, idMovie);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testAddOrDelItemListListNotFound() {
        // Mock data
        String email = "test@example.com";
        Long idList = 1L;
        Long idMovie = 2L;

        // Mock user
        User user = new User();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock repository methods
        Mockito.when(movieListRepository.getById(idList)).thenReturn(null);

        // Test service method
        ResponseEntity<Void> response = movieListService.addOrDelItemLIst(email, idList, idMovie);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddOrDelItemListUserNotFound() {
        // Mock data
        String email = "test@example.com";
        Long idList = 1L;
        Long idMovie = 2L;

        // Mock repository methods
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);

        // Test service method
        ResponseEntity<Void> response = movieListService.addOrDelItemLIst(email, idList, idMovie);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testGetAllListsContainingMovieSuccess() {
        Long idMovie = 1L;
        List<ListMovie> listMovies = new ArrayList<>();
        ListMovie movie1 = new ListMovie("List1", "Content1", new User());
        movie1.setId(1L);
        ListMovie movie2 = new ListMovie("List2", "Content2", new User());
        movie2.setId(2L);
        listMovies.add(movie1);
        listMovies.add(movie2);

        when(movieListRepository.findAllByidMovie(idMovie)).thenReturn(listMovies);
        when(likeListRepository.countByMovieReviewAndLickOrDisIsTrue(any())).thenReturn(1);
        when(likeListRepository.countByMovieReviewAndLickOrDisIsFalse(any())).thenReturn(0);
        when(listGenreRepository.getAllByMovieList(any())).thenReturn(new ArrayList<>());

        ResponseEntity<List<ParsList>> responseEntity = movieListService.getAllListsContainingMovie(idMovie);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
        // Add more assertions based on your expectations
    }

    @Test
    public void testAuthorshipCheckSuccess() {
        Long idList = 1L;
        String username = "testuser";
        User user = new User();
        ListMovie list = new ListMovie("List1", "Content1", user);
        list.setId(idList);

        when(userRepository.findByEmail(username)).thenReturn(user);
        when(movieListRepository.getById(idList)).thenReturn(list);

        ResponseEntity<Boolean> responseEntity = movieListService.authorshipCheck(idList, username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetTop10LatestListsSuccess() {
        List<ListMovie> listMovies = new ArrayList<>();
        ListMovie movie1 = new ListMovie("List1", "Content1", new User());
        ListMovie movie2 = new ListMovie("List2", "Content2", new User());
        listMovies.add(movie1);
        listMovies.add(movie2);

        when(movieListRepository.findTop10LatestLists()).thenReturn(listMovies);
        when(likeListRepository.countByMovieReviewAndLickOrDisIsTrue(any())).thenReturn(1);
        when(likeListRepository.countByMovieReviewAndLickOrDisIsFalse(any())).thenReturn(0);
        when(listGenreRepository.getAllByMovieList(any())).thenReturn(new ArrayList<>());

        ResponseEntity<List<ParsList>> responseEntity = movieListService.getTop10LatestLists();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        // Add more assertions based on your expectations
    }

    @Test
    public void testGetTop10LikedListsSuccess() {
        List<ListMovie> listMovies = new ArrayList<>();
        ListMovie movie1 = new ListMovie("List1", "Content1", new User());
        ListMovie movie2 = new ListMovie("List2", "Content2", new User());
        listMovies.add(movie1);
        listMovies.add(movie2);

        when(movieListRepository.findTop10LikedLists()).thenReturn(listMovies);
        when(likeListRepository.countByMovieReviewAndLickOrDisIsTrue(any())).thenReturn(1);
        when(likeListRepository.countByMovieReviewAndLickOrDisIsFalse(any())).thenReturn(0);
        when(listGenreRepository.getAllByMovieList(any())).thenReturn(new ArrayList<>());

        ResponseEntity<List<ParsList>> responseEntity = movieListService.getTop10LikedLists();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
        // Add more assertions based on your expectations
    }
}


