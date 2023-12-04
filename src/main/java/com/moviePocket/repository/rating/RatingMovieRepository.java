package com.moviePocket.repository.rating;

import com.moviePocket.entities.rating.RatingMovie;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingMovieRepository extends JpaRepository<RatingMovie,Long> {

    RatingMovie findByUserAndMovie_id(User user, Long idMovie);
    List<RatingMovie> findAllByUser(User user);

    @Query("SELECT AVG(rm.rating) FROM RatingMovie rm WHERE rm.movie.id = :movieId")
    Double getAverageRatingByMovieId(@Param("movieId") Long movieId);

    int countAllByMovie_id(Long idMovie);

}
