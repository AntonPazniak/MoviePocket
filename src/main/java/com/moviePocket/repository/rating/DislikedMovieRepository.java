package com.moviePocket.repository.rating;

import com.moviePocket.entities.rating.DislikedMovie;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DislikedMovieRepository extends JpaRepository<DislikedMovie,Long> {

    DislikedMovie findByUserAndMovie_Id(User user, Long idMovie);
    List<DislikedMovie> findAllByUser(User user);

    @Query("SELECT COUNT(u) FROM DislikedMovie u WHERE u.movie.id = :movieId")
    int getAllCountByIdMovie(@Param("movieId") Long idMovie);
}