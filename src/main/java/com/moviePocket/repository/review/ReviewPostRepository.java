package com.moviePocket.repository.review;

import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.review.Review;
import com.moviePocket.entities.review.ReviewPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewPostRepository extends JpaRepository<ReviewPost, Long> {

    ReviewPost findByReview(Review review);

    int countByPost_Id(Long idPost);

    @Query("SELECT rl.review FROM ReviewPost rl WHERE rl.post = :post")
    List<Review> findReviewsByPost(@Param("post") Post post);


}