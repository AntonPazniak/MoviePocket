package com.moviePocket.repository.movie.review;

import com.moviePocket.entities.movie.list.MovieList;
import com.moviePocket.entities.movie.review.Review;
import com.moviePocket.entities.movie.review.ReviewList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewListRepository extends JpaRepository<ReviewList, Long> {
    ReviewList findByReview_Id(Long idReview);

    @Query("SELECT rl.review FROM ReviewList rl WHERE rl.movieList = :movieList")
    List<Review> findReviewsByMovieList(@Param("movieList") MovieList movieList);

    int countByMovieList_Id(Long idList);

    ReviewList findByReview(Review review);

}