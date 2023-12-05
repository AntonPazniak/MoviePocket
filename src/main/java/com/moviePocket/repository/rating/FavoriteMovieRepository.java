package com.moviePocket.repository.rating;

import com.moviePocket.entities.rating.FavoriteMovie;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie,Long> {

    FavoriteMovie findByUserAndMovie_id(User user, Long idMovie);
    List<FavoriteMovie> findAllByUser(User user);

    @Query("SELECT COUNT(u) FROM FavoriteMovie u WHERE u.movie.id = :movieId")
    int getAllCountByIdMovie(@Param("movieId") Long idMovie);



}