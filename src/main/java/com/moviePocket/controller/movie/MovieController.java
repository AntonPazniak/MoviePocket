/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.movie;

import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.reaction.RatingMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieServiceImpl movieService;
    private final RatingMovieService ratingMovieService;

//    @GetMapping("/{idMovie}")
//    public ResponseEntity<MovieDto> getMovieInfo(@PathVariable("idMovie") Long idMovie) {
//        MovieDto movieDto = new MovieDto(
//                idMovie,
//                ratingMovieService.getMovieRating(idMovie).getBody(),
//                ratingMovieService.getAllCountByIdMovie(idMovie).getBody(),
//                dislikedMovieService.getAllCountByIdMovie(idMovie).getBody(),
//                favoriteMovieService.getAllCountByIdMovie(idMovie).getBody(),
//                toWatchMovieService.getAllCountByIdMovie(idMovie).getBody(),
//                watchedMovieService.getAllCountByIdMovie(idMovie)
//        );
//        return new ResponseEntity<>(movieDto, HttpStatus.OK);
//    }

//    @GetMapping("/{idMovie}")
//    public ResponseEntity<Void> getMovieInfo(@PathVariable("idMovie") Long idMovie) {
//        movieService.setMovie(idMovie);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }


}
