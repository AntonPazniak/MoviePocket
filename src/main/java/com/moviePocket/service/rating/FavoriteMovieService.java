package com.moviePocket.service.rating;

import com.moviePocket.entities.movie.Movie;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FavoriteMovieService {

    ResponseEntity<Void> setOrDeleteNewFavoriteMovies(String email, Long idMovie);

    ResponseEntity<Boolean> getFromFavoriteMovies(String email, Long idMovie);

    public ResponseEntity<List<Movie>> getAllUserFavoriteMovies(String email);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);
}
