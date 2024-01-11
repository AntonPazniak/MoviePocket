/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.rating;

import com.moviePocket.entities.rating.Rating;
import com.moviePocket.service.inter.rating.RatingMovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/rating")
@Api(value = "Rating Movie Controller", tags = "Controller to rate movies, avr rating is double value")
public class RatingMovieController {

    @Autowired
    RatingMovieService ratingMovieService;

    @PostMapping("/set")
    @ApiOperation(value = "Set the rating for a movie", notes = "Ff user already set rating for the movie it will be updated")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully set the rating"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "User is not authentificated")
    })
    public ResponseEntity<Void> setRatingMovie(
            @RequestParam("idMovie") Long idMovie,
            @RequestParam("rating") int rating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ratingMovieService.setNewRatingMovie(authentication.getName(), idMovie, rating);
    }

    @PostMapping("/del")
    @ApiOperation("Remove the rating for a movie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully removed the rating"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "User is not authentificated")
    })
    public ResponseEntity<Void> delRatingMovie(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ratingMovieService.removeFromRatingMovie(authentication.getName(), idMovie);
    }

    @GetMapping("/get")
    @ApiOperation("Get the rating for a movie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the rating"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "User is not authentificated")
    })
    public ResponseEntity<Integer> getUserRatingMovie(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ratingMovieService.getFromRatingMovie(authentication.getName(), idMovie);
    }

    @GetMapping("/allByUser")
    @ApiOperation("Get all ratings for a user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the users' ratings"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "User is not authentificated")
    })
    public ResponseEntity<List<Rating>> allRatingByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ratingMovieService.getAllUserRatingMovie(authentication.getName());
    }

    @ApiOperation(value = "Get double number which represents the current rating of the movie(avr of all from users)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved (0.0 if it was now rated yet "),
            @ApiResponse(code = 400, message = "Smth wrong"),

    })
    @GetMapping("/getByIdMovie")
    public ResponseEntity<Double> getByIdMovieRating(@RequestParam("idMovie") Long idMovie) {
        return ratingMovieService.getMovieRating(idMovie);
    }

    @ApiOperation(value = "Get int number of times the movie was rated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved number "),
            @ApiResponse(code = 400, message = "Smth wrong"),

    })
    @GetMapping("/count/rating")
    public ResponseEntity<Integer> getCountMovieRating(@RequestParam("idMovie") Long idMovie) {
        return ratingMovieService.getAllCountByIdMovie(idMovie);
    }

}