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
import com.moviePocket.service.inter.rating.DislikedMovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(DislikedMovieController.class);
    @Autowired
    DislikedMovieService dislikedMovieService;

    @PostMapping("/set")
    @Operation(summary = "Set or delete a movie from the disliked list", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set or deleted the movie"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> setOrDeleteMovieWatched(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.toString());
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
