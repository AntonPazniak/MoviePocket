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
import com.moviePocket.service.inter.rating.WatchedMovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/watched")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Watched Movie Controller", description = "Controller for managing watched movies")
public class WatchedMovieController {

    @Autowired
    private WatchedMovieService watchedMovieService;

    @Operation(summary = "Set or delete a movie from watched list", description = "Allows setting or deleting a movie from the watched list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set or deleted the movie as watched"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/set")
    public void setOrDeleteMovieWatched(@RequestParam("idMovie") Long idMovie, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        watchedMovieService.setOrDeleteNewWatched(authentication.getName(), idMovie);
    }

    @Operation(summary = "Check if a movie is watched by the user", description = "Checks if a specific movie is in the user's watched list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the movie watch status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/get")
    public boolean getIsMovieWatchedByUser(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return watchedMovieService.getFromWatched(authentication.getName(), idMovie);
    }

    @Operation(summary = "Get all movies watched by the user", description = "Retrieves all movies that have been watched by the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all watched movies"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/allByUser")
    public List<Movie> allUserMovieWatchedMovies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return watchedMovieService.getAllUserWatched(authentication.getName());
    }

    @Operation(summary = "Get the number of times the movie was added to watched", description = "Retrieves the count of how many times a movie has been added to the watched list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the count"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/count/watched")
    public Integer getAllCountWatchedByIdMovie(@RequestParam("idMovie") Long id) {
        return watchedMovieService.getAllCountByIdMovie(id);
    }

    // Uncomment and update the following method as needed:
    /*
    @Operation(summary = "Get the number of movies added to the watched list from a specific movie list", description = "Retrieves the count of watched movies from a specific list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the count"),
            @ApiResponse(responseCode = "400", description = "Invalid movie list ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/count/watched/fromList")
    public ResponseEntity<Integer> getCountWatchedFromList(
            @RequestParam("idMovieList") Long idMovieList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return watchedMovieService.getCountWatchedFromList(authentication.getName(), idMovieList);
    }
    */
}
