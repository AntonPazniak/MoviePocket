package com.moviePocket.repository.movie.review;

import com.moviePocket.entities.movie.review.Review;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public interface MovieReviewRepository extends JpaRepository<Review, Long> {

    Review getById(Long id);

    ArrayList<Review> getAllByIdMovie(Long id);

    List<Review> getAllByUser(User user);

    List<Review> getAllByUserAndIdMovie(User user, Long idMovie);

    @Query("SELECT COUNT(u) FROM Review u WHERE u.idMovie = :movieId")
    int getAllCountByIdMovie(@Param("movieId") Long idMovie);

    @Query("SELECT COUNT(u) FROM Review u WHERE u.idMovie = :id_user")
    int getAllCountByUser(@Param("id_user") User user);


}
