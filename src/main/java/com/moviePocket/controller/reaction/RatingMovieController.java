/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.reaction;

import com.moviePocket.db.entities.rating.Rating;
import com.moviePocket.service.inter.reaction.RatingMovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/rating")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Rating Movie Controller", description = "Controller to rate movies, average rating is double value")
public class RatingMovieController {

    private RatingMovieService ratingMovieService;

    @PostMapping("/my/set")
    @Operation(summary = "Set the rating for a movie", description = "If user already set rating for the movie, it will be updated")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully set the rating"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    public ResponseEntity<Object> setRatingMovie(
            @RequestParam("idMovie") Long idMovie,
            @RequestParam("rating") int rating) {
        ratingMovieService.setRating(idMovie, rating);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/my/del")
    @Operation(summary = "Remove the rating for a movie", description = "Removes the rating for the specified movie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully removed the rating"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    public ResponseEntity<Object> delRatingMovie(@RequestParam("idMovie") Long idMovie) {
        ratingMovieService.removeRating(idMovie);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/my/get")
    @Operation(summary = "Get the rating for a movie", description = "Retrieves the rating set by the user for the specified movie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the rating"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    public ResponseEntity<Integer> getUserRatingMovie(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(ratingMovieService.getMyRatingByIdMovie(idMovie));
    }

    @GetMapping("/my/all")
    @Operation(summary = "Get all ratings for a user", description = "Retrieves all ratings given by the user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user's ratings"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated")
    })
    public ResponseEntity<List<Rating>> allRatingByUser() {
        return ResponseEntity.ok(ratingMovieService.getAllUserRating());
    }

    @GetMapping("/get")
    @Operation(summary = "Get average rating for a movie", description = "Retrieves the average rating for the specified movie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the average rating (0.0 if it was not rated yet)"),
            @ApiResponse(responseCode = "400", description = "Something went wrong")
    })
    public ResponseEntity<Float> getByIdMovieRating(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(ratingMovieService.getRatingByIdMovie(idMovie));
    }

    @GetMapping("/count")
    @Operation(summary = "Get the number of times the movie was rated", description = "Retrieves the count of ratings for the specified movie")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the number of ratings"),
            @ApiResponse(responseCode = "400", description = "Something went wrong")
    })
    public ResponseEntity<Integer> getCountMovieRating(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(ratingMovieService.getRatingCountByIdMovie(idMovie));
    }
}
