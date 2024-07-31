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
import com.moviePocket.db.entities.rating.ToWatchMovie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.reaction.ToWatchMovieRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.reaction.ToWatchMovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ToWatchMovieServiceImplTest {

    @Mock
    private ToWatchMovieRepository toWatchMovieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieServiceImpl movieService;

    @InjectMocks
    private ToWatchMovieServiceImpl toWatchMovieService;

    @Test
    void testSetOrDeleteToWatch_UserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        ResponseEntity<Void> response = toWatchMovieService.setOrDeleteToWatch("test@example.com", 1L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSetOrDeleteToWatch_MovieNotFound() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(movieService.setMovieIfNotExist(Mockito.anyLong())).thenReturn(null);

        ResponseEntity<Void> response = toWatchMovieService.setOrDeleteToWatch("test@example.com", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSetOrDeleteToWatch_AddToWatch() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(movieService.setMovieIfNotExist(Mockito.anyLong())).thenReturn(new Movie());
        Mockito.when(toWatchMovieRepository.findByUserAndMovie_Id(user, 1L)).thenReturn(null);

        ResponseEntity<Void> response = toWatchMovieService.setOrDeleteToWatch("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSetOrDeleteToWatch_DeleteFromToWatch() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(movieService.setMovieIfNotExist(Mockito.anyLong())).thenReturn(new Movie());
        Mockito.when(toWatchMovieRepository.findByUserAndMovie_Id(user, 1L)).thenReturn(new ToWatchMovie(user, new Movie()));

        ResponseEntity<Void> response = toWatchMovieService.setOrDeleteToWatch("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetFromToWatch_UserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        ResponseEntity<Boolean> response = toWatchMovieService.getFromToWatch("test@example.com", 1L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetFromToWatch_MovieNotInToWatch() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(toWatchMovieRepository.findByUserAndMovie_Id(user, 1L)).thenReturn(null);

        ResponseEntity<Boolean> response = toWatchMovieService.getFromToWatch("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void testGetFromToWatch_MovieInToWatch() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(toWatchMovieRepository.findByUserAndMovie_Id(user, 1L)).thenReturn(new ToWatchMovie(user, new Movie()));

        ResponseEntity<Boolean> response = toWatchMovieService.getFromToWatch("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testGetAllUserToWatch_UserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        ResponseEntity<List<Movie>> response = toWatchMovieService.getAllUserToWatch("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetAllUserToWatch_NoMoviesInToWatch() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        Mockito.when(toWatchMovieRepository.findAllByUserOrderByCreatedAsc(user)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Movie>> response = toWatchMovieService.getAllUserToWatch("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetAllUserToWatch_MoviesInToWatch() {
        User user = new User();
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        List<ToWatchMovie> toWatchList = new ArrayList<>();
        toWatchList.add(new ToWatchMovie(user, new Movie()));

        Mockito.when(toWatchMovieRepository.findAllByUserOrderByCreatedAsc(user)).thenReturn(toWatchList);

        ResponseEntity<List<Movie>> response = toWatchMovieService.getAllUserToWatch("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAllCountByIdMovie() {
        Mockito.when(toWatchMovieRepository.getAllCountByIdMovie(Mockito.anyLong())).thenReturn(5);

        ResponseEntity<Integer> response = toWatchMovieService.getAllCountByIdMovie(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody());
    }
}

