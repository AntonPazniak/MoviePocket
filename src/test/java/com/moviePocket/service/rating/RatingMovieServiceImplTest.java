/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.rating;

import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.rating.Rating;
import com.moviePocket.db.entities.rating.RatingMovie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.rating.RatingMovieRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.reaction.RatingMovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
public class RatingMovieServiceImplTest {

    @Mock
    private RatingMovieRepository ratingMovieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieServiceImpl movieService;

    @InjectMocks
    private RatingMovieServiceImpl ratingMovieService;

    @Test
    void testSetNewRatingMovie_Success() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        Movie movie = new Movie();
        Mockito.when(movieService.setMovieIfNotExist(anyLong())).thenReturn(movie);

        Mockito.when(ratingMovieRepository.findByUserAndMovie_id(user, 1L)).thenReturn(null);

        ResponseEntity<Void> response = ratingMovieService.setNewRatingMovie("test@example.com", 1L, 8);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSetNewRatingMovie_UserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        ResponseEntity<Void> response = ratingMovieService.setNewRatingMovie("nonexistent@example.com", 1L, 8);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testSetNewRatingMovie_MovieNotFound() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        Mockito.when(movieService.setMovieIfNotExist(anyLong())).thenReturn(null);

        ResponseEntity<Void> response = ratingMovieService.setNewRatingMovie("test@example.com", 1L, 8);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSetNewRatingMovie_InvalidRating() {
        ResponseEntity<Void> response = ratingMovieService.setNewRatingMovie("test@example.com", 1L, 15);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRemoveFromRatingMovie_Success() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        RatingMovie ratingMovie = new RatingMovie(user, new Movie(), 5);
        Mockito.when(ratingMovieRepository.findByUserAndMovie_id(user, 1L)).thenReturn(ratingMovie);

        ResponseEntity<Void> response = ratingMovieService.removeFromRatingMovie("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRemoveFromRatingMovie_UserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        ResponseEntity<Void> response = ratingMovieService.removeFromRatingMovie("nonexistent@example.com", 1L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testRemoveFromRatingMovie_RatingMovieNotFound() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        Mockito.when(ratingMovieRepository.findByUserAndMovie_id(user, 1L)).thenReturn(null);

        ResponseEntity<Void> response = ratingMovieService.removeFromRatingMovie("test@example.com", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetFromRatingMovie_Success() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        RatingMovie ratingMovie = new RatingMovie(user, new Movie(), 8);
        Mockito.when(ratingMovieRepository.findByUserAndMovie_id(user, 1L)).thenReturn(ratingMovie);

        ResponseEntity<Integer> response = ratingMovieService.getFromRatingMovie("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(8, response.getBody());
    }

    @Test
    void testGetFromRatingMovie_UserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        ResponseEntity<Integer> response = ratingMovieService.getFromRatingMovie("nonexistent@example.com", 1L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetFromRatingMovie_RatingMovieNotFound() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        Mockito.when(ratingMovieRepository.findByUserAndMovie_id(user, 1L)).thenReturn(null);

        ResponseEntity<Integer> response = ratingMovieService.getFromRatingMovie("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody());
    }

    @Test
    void testGetAllUserRatingMovie_Success() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        List<RatingMovie> ratingMovieList = new ArrayList<>();
        ratingMovieList.add(new RatingMovie(user, new Movie(), 6));

        Mockito.when(ratingMovieRepository.findAllByUser(user)).thenReturn(ratingMovieList);

        ResponseEntity<List<Rating>> response = ratingMovieService.getAllUserRatingMovie("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAllUserRatingMovie_UserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        ResponseEntity<List<Rating>> response = ratingMovieService.getAllUserRatingMovie("nonexistent@example.com");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetAllUserRatingMovie_NoRatingMovies() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        Mockito.when(ratingMovieRepository.findAllByUser(user)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Rating>> response = ratingMovieService.getAllUserRatingMovie("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetMovieRating_Success() {
        Mockito.when(ratingMovieRepository.getAverageRatingByMovieId(anyLong())).thenReturn(7.8);

        ResponseEntity<Double> response = ratingMovieService.getMovieRating(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7.8, response.getBody());
    }

    @Test
    void testGetMovieRating_NoRatings() {
        Mockito.when(ratingMovieRepository.getAverageRatingByMovieId(anyLong())).thenReturn(null);

        ResponseEntity<Double> response = ratingMovieService.getMovieRating(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0, response.getBody());
    }

    @Test
    void testGetAllCountByIdMovie_Success() {
        Mockito.when(ratingMovieRepository.countAllByMovie_id(anyLong())).thenReturn(15);

        ResponseEntity<Integer> response = ratingMovieService.getAllCountByIdMovie(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(15, response.getBody());
    }

}

