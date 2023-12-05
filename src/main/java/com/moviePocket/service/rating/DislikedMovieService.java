package com.moviePocket.service.rating;

import com.moviePocket.entities.movie.Movie;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DislikedMovieService {

    ResponseEntity<Void> setOrDeleteDislikedMovie(String email, Long idMovie);

    ResponseEntity<Boolean> getFromDislikedMovie(String email, Long idMovie);

    ResponseEntity<List<Movie>> getAllUserDislikedMovie(String email);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);

}