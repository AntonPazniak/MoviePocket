/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.list;

import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface MovieListRepository extends JpaRepository<ListMovie, Long> {

    ListMovie getById(Long id);

    List<ListMovie> findAllByUser(User user);

    @Query("SELECT lm FROM ListMovie lm WHERE LOWER(lm.title) LIKE LOWER(CONCAT('%', :partialTitle, '%'))")
    List<ListMovie> findAllByPartialTitle(@Param("partialTitle") String partialTitle);


    @Query("SELECT lm FROM ListMovie lm JOIN lm.movies movie WHERE movie.id = :idMovie")
    List<ListMovie> findAllByidMovie(@Param("idMovie") Long idMovie);

    @Query("SELECT lm FROM ListMovie lm ORDER BY lm.created DESC")
    List<ListMovie> findTop10LatestLists();

    @Query("SELECT ll.movieList, COUNT(ll) as likeCount " +
            "FROM LikeList ll " +
            "WHERE ll.lickOrDis = true " +
            "GROUP BY ll.movieList " +
            "HAVING COUNT(ll) > 0 " +
            "ORDER BY likeCount DESC")
    List<ListMovie> findTop10LikedLists();


}