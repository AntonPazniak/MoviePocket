package com.moviePocket.controller.movie;

import com.moviePocket.controller.dto.MovieDto;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.list.MovieListService;
import com.moviePocket.service.rating.*;
import com.moviePocket.service.raview.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieServiceImpl movieService;
    private final RatingMovieService ratingMovieService;
    private final DislikedMovieService dislikedMovieService;
    private final FavoriteMovieService favoriteMovieService;
    private final WatchedMovieService watchedMovieService;
    private final ToWatchMovieService toWatchMovieService;
    private final ReviewService reviewService;
    private final MovieListService movieListService;



    @GetMapping("/{idMovie}")
    public ResponseEntity<MovieDto> getMovieInfo(@PathVariable("idMovie") Long idMovie) {
        MovieDto movieDto = new MovieDto(
                idMovie,
                ratingMovieService.getMovieRating(idMovie).getBody(),
                ratingMovieService.getAllCountByIdMovie(idMovie).getBody(),
                dislikedMovieService.getAllCountByIdMovie(idMovie).getBody(),
                favoriteMovieService.getAllCountByIdMovie(idMovie).getBody(),
                toWatchMovieService.getAllCountByIdMovie(idMovie).getBody(),
                watchedMovieService.getAllCountByIdMovie(idMovie).getBody()
        );
        return new ResponseEntity<>(movieDto, HttpStatus.OK);
    }

//    @GetMapping("/{idMovie}")
//    public ResponseEntity<Void> getMovieInfo(@PathVariable("idMovie") Long idMovie) {
//        movieService.setMovie(idMovie);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }



}
