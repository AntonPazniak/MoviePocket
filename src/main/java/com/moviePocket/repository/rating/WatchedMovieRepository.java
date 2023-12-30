package com.moviePocket.repository.rating;


import com.moviePocket.entities.rating.WatchedMovie;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, Long> {

    WatchedMovie findByUserAndMovie_Id(User user, Long idMovie);

    List<WatchedMovie> findAllByUser(User user);

    @Query("SELECT COUNT(u) FROM WatchedMovie u WHERE u.movie.id = :movieId")
    int getAllCountByIdMovie(@Param("movieId") Long idMovie);

    @Query("SELECT COUNT(wm) FROM WatchedMovie wm WHERE wm.user = :user AND wm.movie.id IN (SELECT m.id FROM ListMovie lm JOIN lm.movies m WHERE lm.id = :listMovieId)")
    int getCountWatchedFromList(@Param("user") User user, @Param("listMovieId") Long listMovieId);
}
