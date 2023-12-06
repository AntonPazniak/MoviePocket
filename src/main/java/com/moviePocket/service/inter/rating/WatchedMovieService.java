package com.moviePocket.service.inter.rating;

import com.moviePocket.entities.movie.Movie;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WatchedMovieService {

    ResponseEntity<Void> setOrDeleteNewWatched(String email, Long idMovie);

    ResponseEntity<Boolean> getFromWatched(String email, Long idMovie);

    ResponseEntity<List<Movie>> getAllUserWatched(String email);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);

}
