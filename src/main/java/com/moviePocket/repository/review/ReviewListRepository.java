package com.moviePocket.repository.review;

import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.review.Review;
import com.moviePocket.entities.review.ReviewList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewListRepository extends JpaRepository<ReviewList, Long> {
    ReviewList findByReview_Id(Long idReview);

    @Query("SELECT rl.review FROM ReviewList rl WHERE rl.movieList = :movieList")
    List<Review> findReviewsByMovieList(@Param("movieList") ListMovie movieList);

    int countByMovieList_Id(Long idList);

    ReviewList findByReview(Review review);

}