/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.entities.user;

import com.moviePocket.entities.list.ParsList;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.rating.Rating;
import com.moviePocket.entities.review.ParsReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsUserPage {

    private String username;
    private String bio;
    private Date created;
    private Long avatar;
    private List<ParsList> lists;
    private List<Movie> likeMovie;
    private List<Movie> dislikeMovie;
    private List<Movie> watchedMovie;
    private List<Movie> toWatchMovie;
    private List<ParsReview> reviews;
    private List<Rating> ratingMovie;
}