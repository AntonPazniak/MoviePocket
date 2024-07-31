/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.rating;

import com.moviePocket.db.repository.list.MovieListRepository;
import com.moviePocket.db.repository.rating.WatchedMovieRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.reaction.WatchedMovieServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WatchedMovieServiceImplTest {

    @Mock
    private WatchedMovieRepository watchedMovieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieServiceImpl movieService;

    @Mock
    private MovieListRepository movieListRepository;

    @InjectMocks
    private WatchedMovieServiceImpl watchedMovieService;

//    @Test
//    void testGetFromWatched_UserNotFound() {
//        // Mock user not found
//        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);
//
//        ResponseEntity<Boolean> response = watchedMovieService.getFromWatched("nonexistent@example.com", 1L);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//        assertNull(response.getBody());
//    }
//
//
//    @Test
//    void testGetAllUserWatched_UserFoundWithWatchedMovies() {
//        User user = new User();
//        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
//
//        List<WatchedMovie> watchedList = new ArrayList<>();
//        watchedList.add(new WatchedMovie(user, new Movie()));
//
//        Mockito.when(watchedMovieRepository.findAllByUserOrderByCreatedAsc(user.getEmail())).thenReturn(watchedList);
//
//        ResponseEntity<List<Movie>> response = watchedMovieService.getAllUserWatched("test@example.com");
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(1, response.getBody().size());
//    }
//
//    @Test
//    void testGetAllCountByIdMovie() {
//        int count = 5;
//        Mockito.when(watchedMovieRepository.getAllCountByIdMovie(1L)).thenReturn(count);
//
//        ResponseEntity<Integer> response = watchedMovieService.getAllCountByIdMovie(1L);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(count, response.getBody());
//    }
//
//    @Test
//    void testGetCountWatchedFromList() {
//        User user = new User();
//        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(user);
//
//        ListMovie movieList = new ListMovie();
//        Mockito.when(movieListRepository.getById(1L)).thenReturn(movieList);
//
//        int count = 3;
//        Mockito.when(watchedMovieRepository.getCountWatchedFromList(user, 1L)).thenReturn(count);
//
//        ResponseEntity<Integer> response = watchedMovieService.getCountWatchedFromList("test@example.com", 1L);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(count, response.getBody());
//    }
}


