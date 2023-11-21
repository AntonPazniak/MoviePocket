package com.moviePocket.service.movie.raview;

import com.moviePocket.entities.movie.review.ParsReview;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MovieReviewService {

    ResponseEntity<Void> createMovieReview(String email, Long idMovie, String title, String content);

    ResponseEntity<Void> updateReview(Long idReview, String username, String title, String content);

    ResponseEntity<List<ParsReview>> getAllByIDMovie(Long idMovie);

    ResponseEntity<Void> delReview(Long idReview, String username);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);

    ResponseEntity<ParsReview> getByIdReview(Long idReview);

    ResponseEntity<Boolean> authorshipCheck(Long idReview, String username);

}
