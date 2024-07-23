/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.list;

import com.moviePocket.db.entities.list.LikeList;
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface LikeListRepository extends JpaRepository<LikeList, Long> {
    LikeList getByUserAndMovieList(User user, ListMovie movieList);

    @Query("SELECT COUNT(lmr) FROM LikeList lmr WHERE lmr.movieList = :movieList AND lmr.lickOrDis = true")
    int countByMovieReviewAndLickOrDisIsTrue(@Param("movieList") ListMovie movieList);

    @Query("SELECT COUNT(lmr) FROM LikeList lmr WHERE lmr.movieList = :movieList AND lmr.lickOrDis = false")
    int countByMovieReviewAndLickOrDisIsFalse(@Param("movieList") ListMovie movieList);

    void deleteAllByMovieList(ListMovie movieList);

}
