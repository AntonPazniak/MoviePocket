package com.moviePocket.repository.review;

import com.moviePocket.entities.review.LikeMovieReview;
import com.moviePocket.entities.review.Review;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface LikeReviewRepository extends JpaRepository<LikeMovieReview, Long> {

    LikeMovieReview getByUserAndReview(User user, Review review);

    void deleteAllByReview(Review review);

    @Query("SELECT COUNT(lmr) FROM LikeMovieReview lmr WHERE lmr.review = :movieReview AND lmr.lickOrDis = true")
    int countByMovieReviewAndLickOrDisIsTrue(Review movieReview);

    @Query("SELECT COUNT(lmr) FROM LikeMovieReview lmr WHERE lmr.review = :movieReview AND lmr.lickOrDis = false")
    int countByMovieReviewAndLickOrDisIsFalse(Review movieReview);


}
