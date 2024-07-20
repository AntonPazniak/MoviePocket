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
import com.moviePocket.service.inter.rating.DislikedMovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Disliked Movie Controller", description = "Controller to dislike a movie")
@RequestMapping("/movies/dislike")
public class DislikedMovieController {

    @Autowired
    DislikedMovieService dislikedMovieService;

    @Operation(summary = "Set or delete a movie from the disliked list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set or deleted the movie"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/set")
    public ResponseEntity<Void> setOrDeleteMovieWatched(@RequestParam("idMovie") Long idMovie, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return dislikedMovieService.setOrDeleteDislikedMovie(authentication.getName(), idMovie);
    }

    @Operation(summary = "Check if a user has disliked a movie", description = "Returns boolean")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the result"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/get")
    public ResponseEntity<Boolean> getIsUserDislikedMovie(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return dislikedMovieService.getFromDislikedMovie(authentication.getName(), idMovie);
    }

    @Operation(summary = "Get all movies disliked by a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> allUserDislikedMovies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return dislikedMovieService.getAllUserDislikedMovie(authentication.getName());
    }

    @Operation(summary = "Get the number of times the movie was added to disliked")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved number"),
            @ApiResponse(responseCode = "400", description = "Something went wrong"),
    })
    @GetMapping("/count/dislike")
    public ResponseEntity<Integer> getAllCountDislikedByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return dislikedMovieService.getAllCountByIdMovie(idMovie);
    }
}
