package com.moviePocket.repository.tracking;

import com.moviePocket.entities.tracking.Tracking;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TrackingRepository extends JpaRepository<Tracking, Long> {

    Boolean existsByUserAndIdMovie(User user, long idMovie);

    Tracking findByUserAndIdMovie(User user, long idMovie);

    List<Tracking> findByDateRelease(Date dateRelease);

    List<Tracking> findAllByUser(User user);

    @Query("SELECT COUNT(t) FROM Tracking t WHERE t.idMovie = :idMovie")
    Long countByIdMovie(@Param("idMovie") long idMovie);
}
