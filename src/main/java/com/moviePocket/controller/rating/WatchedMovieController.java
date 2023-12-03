package com.moviePocket.controller.rating;

import com.moviePocket.service.movie.rating.WatchedMovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/movies/watched")
@Api(value = "Watched Movie Controller")
public class WatchedMovieController {

    @Autowired
    WatchedMovieService watchedMovieService;

    @PostMapping("/set")
    @ApiOperation("Set or delete a movie from watched list")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully set or deleted the movie as watched"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Void> setOrDeleteMovieWatched(@RequestParam("idMovie") Long idMovie, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return watchedMovieService.setOrDeleteNewWatched(authentication.getName(), idMovie);
    }

    @GetMapping("/get")
    @ApiOperation("Check if a movie is watched by the user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the movie watch status"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<Boolean> getIsMovieWatchedByUser(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return watchedMovieService.getFromWatched(
                authentication.getName(), idMovie);
    }

    @GetMapping("/allByUser")
    @ApiOperation("Get all movies watched by the user")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved all watched movies"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public ResponseEntity<List<Long>> allUserMovieWatchedMovies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return watchedMovieService.getAllUserWatched(
                authentication.getName());
    }

    @ApiOperation(value = "Get int number of times the movie was added to watched")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved number "),
            @ApiResponse(code = 400, message = "Smth wrong"),

    })
    @GetMapping("/count/watched")
    public ResponseEntity<Integer> getAllCountWatchedByIdMovie(@RequestParam("idMovie") Long id) {
        return watchedMovieService.getAllCountByIdMovie(id);
    }
}
