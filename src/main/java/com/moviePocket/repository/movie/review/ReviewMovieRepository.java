package com.moviePocket.repository.movie.review;

import com.moviePocket.entities.movie.review.Review;
import com.moviePocket.entities.movie.review.ReviewMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ReviewMovieRepository extends JpaRepository<ReviewMovie, Long> {


    @Query("SELECT rm.review FROM ReviewMovie rm WHERE rm.idMovie = :idMovie")
    List<Review> findReviewsByMovieId(@Param("idMovie") Long idMovie);

    int countByIdMovie(Long idMovie);

    Boolean existsByReview(Review review);

    ReviewMovie findByReview(Review review);
}
