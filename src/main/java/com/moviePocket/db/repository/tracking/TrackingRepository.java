/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.tracking;

import com.moviePocket.db.entities.tracking.Tracking;
import com.moviePocket.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrackingRepository extends JpaRepository<Tracking, Long> {

    Boolean existsByUserAndMovie_Id(User user, long idMovie);

    Tracking findByUserAndMovie_Id(User user, long idMovie);

    List<Tracking> findByDateRelease(LocalDate date);

    List<Tracking> findAllByUser(User user);

    Long countAllByMovie_id(Long idMovie);
}
