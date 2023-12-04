package com.moviePocket.repository.rating;

import com.moviePocket.entities.rating.ToWatchMovie;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToWatchMovieRepository extends JpaRepository<ToWatchMovie,Long> {

    ToWatchMovie findByUserAndMovie_Id(User user, Long idMovie);

    List<ToWatchMovie> findAllByUser(User user);

    @Query("SELECT COUNT(u) FROM ToWatchMovie u WHERE u.movie.id = :movieId")
    int getAllCountByIdMovie(@Param("movieId") Long idMovie);


}
