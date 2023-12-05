package com.moviePocket.controller.rating;

import com.moviePocket.entities.movie.Movie;
import com.moviePocket.service.rating.ToWatchMovieService;
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
@RequestMapping("/movies/towatch")
public class ToWatchMovieController {

    @Autowired
    ToWatchMovieService toWatchMovieService;

    @ApiOperation("Set or delete a movie from ToWatch list")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully set or deleted the movie as toWatch"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping("/set")
    public ResponseEntity<Void> setOrDeleteMovieToWatch(@RequestParam("idMovie") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return toWatchMovieService.setOrDeleteToWatch(authentication.getName(), id);
    }

    @ApiOperation("Check if a movie is added toWatch by the user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the movie Towatch status"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/get")
    public ResponseEntity<Boolean> getIsUserMovieToWatch(@RequestParam("idMovie") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return toWatchMovieService.getFromToWatch(
                authentication.getName(), id);
    }

    @ApiOperation("Get all movies ToWatch by the user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved all ToWatch movies"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> allUserMovieToWatchMovies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return toWatchMovieService.getAllUserToWatch(
                authentication.getName());
    }

    @ApiOperation(value = "Get int number of times the movie was added toWatch")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved number "),
            @ApiResponse(code = 400, message = "Smth wrong"),

    })
    @GetMapping("/count/toWatch")
    public ResponseEntity<Integer> getAllCountToWatchByIdMovie(@RequestParam("idMovie") Long id) {
        return toWatchMovieService.getAllCountByIdMovie(id);
    }


}
