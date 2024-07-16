/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.entities.list;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.entities.movie.Genre;
import com.moviePocket.entities.movie.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsList {

    private Long id;
    private String title;
    private String content;
    private Long poster;
    private List<Genre> genres;
    private List<Movie> movies;
    private int[] likeOrDis;
    private UserPostDto user;
    private LocalDateTime create;
    private LocalDateTime update;

}
