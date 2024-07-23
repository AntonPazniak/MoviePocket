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
import com.moviePocket.db.entities.rating.FavoriteMovie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.rating.FavoriteMovieRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.rating.FavoriteMovieServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FavoriteMovieServiceImplTest {

    @Mock
    private FavoriteMovieRepository favoriteMovieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieServiceImpl movieService;

    @InjectMocks
    private FavoriteMovieServiceImpl favoriteMovieService;

    @Test
    void testSetOrDeleteNewFavoriteMovies_UserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        ResponseEntity<Void> response = favoriteMovieService.setOrDeleteNewFavoriteMovies("nonexistent@example.com", 1L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testSetOrDeleteNewFavoriteMovies_MovieNotFound() {
        User user = new User();
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        when(movieService.setMovieIfNotExist(anyLong())).thenReturn(null);

        ResponseEntity<Void> response = favoriteMovieService.setOrDeleteNewFavoriteMovies("test@example.com", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSetOrDeleteNewFavoriteMovies_AddFavorite() {
        User user = new User();
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        when(movieService.setMovieIfNotExist(anyLong())).thenReturn(new Movie());

        ResponseEntity<Void> response = favoriteMovieService.setOrDeleteNewFavoriteMovies("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSetOrDeleteNewFavoriteMovies_DeleteFavorite() {
        User user = new User();
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        when(movieService.setMovieIfNotExist(anyLong())).thenReturn(new Movie());
        when(favoriteMovieRepository.findByUserAndMovie_id(user, 1L)).thenReturn(new FavoriteMovie(user, new Movie()));

        ResponseEntity<Void> response = favoriteMovieService.setOrDeleteNewFavoriteMovies("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetFromFavoriteMovies_UserNotFound() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        ResponseEntity<Boolean> response = favoriteMovieService.getFromFavoriteMovies("nonexistent@example.com", 1L);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetFromFavoriteMovies_FavoriteMovieNotFound() {
        User user = new User();
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        when(favoriteMovieRepository.findByUserAndMovie_id(user, 1L)).thenReturn(null);

        ResponseEntity<Boolean> response = favoriteMovieService.getFromFavoriteMovies("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void testGetFromFavoriteMovies_FavoriteMovieFound() {
        User user = new User();
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        when(favoriteMovieRepository.findByUserAndMovie_id(user, 1L)).thenReturn(new FavoriteMovie(user, new Movie()));

        ResponseEntity<Boolean> response = favoriteMovieService.getFromFavoriteMovies("test@example.com", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testGetAllUserFavoriteMovies() {
        User user = new User();
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);

        List<FavoriteMovie> favoriteMoviesList = new ArrayList<>();
        favoriteMoviesList.add(new FavoriteMovie(user, new Movie()));

        // Mocking behavior for findAllByUser
        when(favoriteMovieRepository.findAllByUserOrderByCreatedAsc(user)).thenReturn(favoriteMoviesList);

        ResponseEntity<List<Movie>> response = favoriteMovieService.getAllUserFavoriteMovies("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAllUserFavoriteMovies_NoFavorites() {
        User user = new User();
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
        when(favoriteMovieRepository.findAllByUserOrderByCreatedAsc(user)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Movie>> response = favoriteMovieService.getAllUserFavoriteMovies("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllCountByIdMovie() {
        when(favoriteMovieRepository.getAllCountByIdMovie(1L)).thenReturn(3);

        ResponseEntity<Integer> response = favoriteMovieService.getAllCountByIdMovie(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody());
    }

    // Helper method for readability
    private void assertFalse(Boolean value) {
        assertTrue(!value);
    }
}
