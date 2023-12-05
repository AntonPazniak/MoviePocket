package com.moviePocket.repository.tracking;

import com.moviePocket.entities.tracking.Tracking;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TrackingRepository extends JpaRepository<Tracking, Long> {

    Boolean existsByUserAndMovie_Id(User user, long idMovie);

    Tracking findByUserAndMovie_Id(User user, long idMovie);

    List<Tracking> findByDateRelease(Date dateRelease);

    List<Tracking> findAllByUser(User user);

    Long countAllByMovie_id(Long idMovie);
}
