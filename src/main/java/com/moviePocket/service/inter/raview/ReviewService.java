package com.moviePocket.service.inter.raview;

import com.moviePocket.entities.review.ParsReview;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewService {

    ResponseEntity<Void> createMovieReview(String email, Long idMovie, String title, String content);

    ResponseEntity<Void> updateReview(Long idReview, String username, String title, String content);

    ResponseEntity<List<ParsReview>> getAllByIDMovie(Long idMovie);

    ResponseEntity<Void> delReview(Long idReview, String username);

    ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie);

    ResponseEntity<ParsReview> getByIdReview(Long idReview);

    ResponseEntity<Boolean> authorshipCheck(Long idReview, String username);

    ResponseEntity<Void> createListReview(String email, Long idList, String title, String content);

    ResponseEntity<List<ParsReview>> getAllByIdList(Long idList);

    ResponseEntity<Integer> getCountByIdList(Long idList);

    ResponseEntity<List<ParsReview>> getAllByIdPost(Long idPost);

    ResponseEntity<Integer> getCountByIdPost(Long idList);

    ResponseEntity<Void> createPostReview(String email, Long idPost, String title, String content);

    ResponseEntity<Integer> getCountByUser(String email);

    ResponseEntity<List<ParsReview>> getAllReviewsByUser(String email);
}
