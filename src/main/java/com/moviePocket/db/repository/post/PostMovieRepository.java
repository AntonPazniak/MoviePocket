/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.post;

import com.moviePocket.db.entities.post.Post;
import com.moviePocket.db.entities.post.PostMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PostMovieRepository extends JpaRepository<PostMovie, Long> {

    Optional<PostMovie> findByPost(Post post);

    List<PostMovie> findAllByMovie_Id(Long idMovie);

}