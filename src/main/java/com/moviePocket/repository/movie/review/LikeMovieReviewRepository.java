package com.moviePocket.repository.movie.review;

import com.moviePocket.entities.movie.review.LikeMovieReview;
import com.moviePocket.entities.movie.review.Review;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface LikeMovieReviewRepository extends JpaRepository<LikeMovieReview, Long> {

    LikeMovieReview getByUserAndMovieReview(User user, Review movieReview);

    void deleteAllByMovieReview(Review movieReview);

    @Query("SELECT COUNT(lmr) FROM LikeMovieReview lmr WHERE lmr.movieReview = :movieReview AND lmr.lickOrDis = true")
    int countByMovieReviewAndLickOrDisIsTrue(Review movieReview);

    @Query("SELECT COUNT(lmr) FROM LikeMovieReview lmr WHERE lmr.movieReview = :movieReview AND lmr.lickOrDis = false")
    int countByMovieReviewAndLickOrDisIsFalse(Review movieReview);


}
