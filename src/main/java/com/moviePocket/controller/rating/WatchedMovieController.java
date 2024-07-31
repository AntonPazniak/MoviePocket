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
import com.moviePocket.service.inter.reaction.ReactionMovie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/watched")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Watched Movie Controller", description = "Controller for managing watched movies")
public class WatchedMovieController {

    @Autowired
    @Qualifier("watchedMovieService")
    private ReactionMovie watchedMovieService;

    @Operation(summary = "Set or delete a movie from watched list", description = "Allows setting or deleting a movie from the watched list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set or deleted the movie as watched"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/set")
    public ResponseEntity<Object> setOrDeleteMovieWatched(@RequestParam("idMovie") Long idMovie, HttpServletRequest request) {
        watchedMovieService.setOrDelete(idMovie);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Check if a movie is watched by the user", description = "Checks if a specific movie is in the user's watched list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the movie watch status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/get")
    public ResponseEntity<Boolean> getIsMovieWatchedByUser(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(watchedMovieService.getReaction(idMovie));
    }

    @Operation(summary = "Get all movies watched by the user", description = "Retrieves all movies that have been watched by the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all watched movies"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> allUserMovieWatchedMovies() {
        return ResponseEntity.ok(watchedMovieService.getAllMyReactions());
    }

    @Operation(summary = "Get the number of times the movie was added to watched", description = "Retrieves the count of how many times a movie has been added to the watched list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the count"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/count")
    public ResponseEntity<Integer> getAllCountWatchedByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(watchedMovieService.getCountReactionByIdMovie(idMovie));
    }

}
