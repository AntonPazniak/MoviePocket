/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.rating;

import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.rating.WatchedMovie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.rating.WatchedMovieRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.rating.WatchedMovieService;
import com.moviePocket.service.inter.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WatchedMovieServiceImpl implements WatchedMovieService {

    @Autowired
    private WatchedMovieRepository watchedMovieRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MovieServiceImpl movieService;


    public void setOrDeleteNewWatched(String email, Long idMovie) {
        User user = userService.chekUserAuntByEmail(email);
        if (!watchedMovieRepository.existsByUser_EmailAndMovie_Id(email, idMovie)) {
            Movie movie = movieService.setMovieIfNotExist(idMovie);
            watchedMovieRepository.save(new WatchedMovie(user, movie));
        } else {
            WatchedMovie watchedMovie = watchedMovieRepository.findByUser_EmailAndMovie_Id(email, idMovie);
            watchedMovieRepository.delete(watchedMovie);
        }
    }

    public boolean getFromWatched(String email, Long idMovie) {
        WatchedMovie watched = watchedMovieRepository.findByUser_EmailAndMovie_Id(email, idMovie);
        return watched != null;
    }

    public List<Movie> getAllUserWatched(String email) {
        List<WatchedMovie> watchedList = watchedMovieRepository.findAllByUserOrderByCreatedAsc(email);
        List<Movie> movies = watchedList.stream()
                .map(WatchedMovie::getMovie)
                .collect(Collectors.toList());

        return movies;
    }

    public Integer getAllCountByIdMovie(Long idMovie) {
        return watchedMovieRepository.getAllCountByIdMovie(idMovie);
    }

//    public ResponseEntity<Integer> getCountWatchedFromList(String email, Long idMovieList) {
//        User user = userRepository.findByEmail(email);
//        if (user == null)
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//
//        ListMovie movieList = movieListRepository.getById(idMovieList);
//        if (movieList == null)
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//        int count = watchedMovieRepository.getCountWatchedFromList(user, idMovieList);
//        return ResponseEntity.ok(count);
//    }
}
