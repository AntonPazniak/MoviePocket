package com.moviePocket.repository.movie.review;

import com.moviePocket.entities.movie.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review getById(Long id);


}
