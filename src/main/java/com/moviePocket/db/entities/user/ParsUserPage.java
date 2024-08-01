/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.user;

import com.moviePocket.db.entities.list.ParsList;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.rating.Rating;
import com.moviePocket.db.entities.review.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsUserPage {

    private String username;
    private String bio;
    private LocalDateTime created;
    private Long avatar;
    private List<ParsList> lists;
    private List<Movie> likeMovie;
    private List<Movie> dislikeMovie;
    private List<Movie> watchedMovie;
    private List<Movie> toWatchMovie;
    private List<ReviewDTO> reviews;
    private List<Rating> ratingMovie;
}