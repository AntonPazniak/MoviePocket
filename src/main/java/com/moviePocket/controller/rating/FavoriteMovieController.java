/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.rating;

import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.service.inter.rating.FavoriteMovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/favorite")
public class FavoriteMovieController {

    @Autowired
    private final FavoriteMovieService favoriteMoviesService;

    public FavoriteMovieController(FavoriteMovieService favoriteMoviesService) {
        this.favoriteMoviesService = favoriteMoviesService;
    }

    @Operation(summary = "Set or delete a movie from the favorite list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set or deleted the movie"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @PostMapping("/set")
    public ResponseEntity<Void> setOrDeleteFavoriteMovie(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return favoriteMoviesService.setOrDeleteNewFavoriteMovies(authentication.getName(), idMovie);
    }

    @Operation(summary = "Check if a user has favorited a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the result"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @GetMapping("/get")
    public ResponseEntity<Boolean> getIsUserFavoriteMovie(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return favoriteMoviesService.getFromFavoriteMovies(authentication.getName(), idMovie);
    }

    @Operation(summary = "Get all movies in user's favorite list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> allUserFavoriteMovies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return favoriteMoviesService.getAllUserFavoriteMovies(authentication.getName());
    }

    @Operation(summary = "Get the number of times the movie was added to favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved number"),
            @ApiResponse(responseCode = "400", description = "Something went wrong")
    })
    @GetMapping("/count/favorite")
    public ResponseEntity<Integer> getAllCountFavoriteByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return favoriteMoviesService.getAllCountByIdMovie(idMovie);
    }
}
