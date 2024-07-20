/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.rating;

import com.moviePocket.entities.movie.Movie;
import com.moviePocket.service.inter.rating.ToWatchMovieService;
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
@RequestMapping("/movies/towatch")
public class ToWatchMovieController {

    @Autowired
    private final ToWatchMovieService toWatchMovieService;

    public ToWatchMovieController(ToWatchMovieService toWatchMovieService) {
        this.toWatchMovieService = toWatchMovieService;
    }

    @Operation(summary = "Set or delete a movie from ToWatch list",
            description = "Allows the user to either set or delete a movie from their 'To Watch' list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set or deleted the movie as toWatch"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/set")
    public ResponseEntity<Void> setOrDeleteMovieToWatch(@RequestParam("idMovie") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return toWatchMovieService.setOrDeleteToWatch(authentication.getName(), id);
    }

    @Operation(summary = "Check if a movie is added to Watchlist by the user",
            description = "Checks whether a specified movie is on the user's 'To Watch' list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the movie ToWatch status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/get")
    public ResponseEntity<Boolean> getIsUserMovieToWatch(@RequestParam("idMovie") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return toWatchMovieService.getFromToWatch(authentication.getName(), id);
    }

    @Operation(summary = "Get all movies ToWatch by the user",
            description = "Retrieves all movies that are on the user's 'To Watch' list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all ToWatch movies"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> allUserMovieToWatchMovies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return toWatchMovieService.getAllUserToWatch(authentication.getName());
    }

    @Operation(summary = "Get the number of times a movie was added to Watchlist",
            description = "Returns the count of how many times a specific movie was added to the 'To Watch' list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved number of times added to Watchlist"),
            @ApiResponse(responseCode = "400", description = "Bad request, possibly invalid movie ID")
    })
    @GetMapping("/count/toWatch")
    public ResponseEntity<Integer> getAllCountToWatchByIdMovie(@RequestParam("idMovie") Long id) {
        return toWatchMovieService.getAllCountByIdMovie(id);
    }
}
