package com.moviePocket.service.rating;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WatchedMovieService {

    ResponseEntity<Void> setOrDeleteNewWatched(String email, Long idMovie);

    ResponseEntity<Boolean> getFromWatched(String email, Long idMovie);

    ResponseEntity<List<Long>> getAllUserWatched(String email);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);

}
