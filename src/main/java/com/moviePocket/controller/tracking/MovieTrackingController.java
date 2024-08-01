/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.tracking;

import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.service.inter.tracing.TracingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/tracking")
@RequiredArgsConstructor
public class MovieTrackingController {

    private final TracingService tracingService;

    @Operation(summary = "Toggle tracking status for a movie",
            description = "Sets or deletes tracking for a specified movie by its ID. If the movie is already being tracked, tracking will be removed. If not, tracking will be added.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated tracking status"),
            @ApiResponse(responseCode = "400", description = "Invalid movie ID or movie does not exist"),
            @ApiResponse(responseCode = "401", description = "User is not logged in")
    })
    @PostMapping("/set")
    public ResponseEntity<Object> setOrDeleteTracingMovie(@RequestParam("idMovie") Long idMovie) {
        tracingService.setOrDel(idMovie);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Check if a movie is being tracked",
            description = "Checks whether a movie with the specified ID is being tracked by the currently logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the tracking status"),
            @ApiResponse(responseCode = "400", description = "Invalid movie ID or movie does not exist"),
            @ApiResponse(responseCode = "401", description = "User is not logged in")
    })
    @GetMapping("/get")
    public ResponseEntity<Boolean> existTracingByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(tracingService.getByIdMovie(idMovie));
    }

    @Operation(summary = "Get the number of users tracking a movie",
            description = "Retrieves the count of users who are tracking a specific movie by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the count"),
            @ApiResponse(responseCode = "400", description = "Invalid movie ID or movie does not exist"),
            @ApiResponse(responseCode = "401", description = "User is not logged in")
    })
    @GetMapping("/count")
    public ResponseEntity<Long> getCountTracingByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(tracingService.getCountByIdMovie(idMovie));
    }

    @Operation(summary = "Get all movies tracked by the user",
            description = "Retrieves a list of all movies currently being tracked by the logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of tracked movies"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "User is not logged in")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovieTracingByUser() {
        return ResponseEntity.ok(tracingService.getAllByUser());
    }
}
