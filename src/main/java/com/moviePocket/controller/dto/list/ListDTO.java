/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.dto.list;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.controller.dto.review.ReactionDTO;
import com.moviePocket.controller.dto.review.ReviewDTO;
import com.moviePocket.db.entities.movie.Genre;
import com.moviePocket.db.entities.movie.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListDTO {

    private Long id;
    private String title;
    private String content;
    private Long poster;
    private List<Genre> genres;
    private List<Movie> movies;
    private ReactionDTO reaction;
    private UserPostDto user;
    private List<ReviewDTO> review;
    private LocalDateTime create;
    private LocalDateTime update;

}
