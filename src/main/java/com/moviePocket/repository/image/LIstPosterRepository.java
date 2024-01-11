/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.repository.image;

import com.moviePocket.entities.image.ListPoster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LIstPosterRepository extends JpaRepository<ListPoster, Long> {

}