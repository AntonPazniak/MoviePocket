package com.moviePocket.service.rating;

import com.moviePocket.entities.rating.Rating;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RatingMovieService {

    ResponseEntity<Void> setNewRatingMovie(String email, Long idMovie, int rating);

    ResponseEntity<Void> removeFromRatingMovie(String email, Long idMovie);

    ResponseEntity<Integer> getFromRatingMovie(String email, Long idMovie);

    ResponseEntity<List<Rating>> getAllUserRatingMovie(String email);

    ResponseEntity<Double> getMovieRating(Long idFilm);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);
}
