package com.moviePocket.controller.rating;

import com.moviePocket.entities.movie.Movie;
import com.moviePocket.service.inter.rating.FavoriteMovieService;
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
@Api(value = "Favorite Movie Controller", tags = "Controller for favorites movies list")
@RequestMapping("/movies/favorite")
public class FavoriteMovieController {

    @Autowired
    FavoriteMovieService favoriteMoviesService;

    @ApiOperation(value = "Set or delete a movie from the favorite list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully set or deleted the movie"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "User is not authentificated")
    })
    @PostMapping("/set")
    public ResponseEntity<Void> setOrDeleteFavoriteMovie(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return favoriteMoviesService.setOrDeleteNewFavoriteMovies(authentication.getName(), idMovie);
    }

    @ApiOperation(value = "Check if a user has favorite a movie")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the result"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "User is not authentificated")
    })
    @GetMapping("/get")
    public ResponseEntity<Boolean> getIsUserFavoriteMovie(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return favoriteMoviesService.getFromFavoriteMovies(
                authentication.getName(), idMovie);
    }

    @ApiOperation(value = "Get all movies user's favorite list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the list"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "User is not authentificated")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> allUserFavoriteMovies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return favoriteMoviesService.getAllUserFavoriteMovies(
                authentication.getName());
    }

    @ApiOperation(value = "Get int number of times the movie was added to fav")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved number "),
            @ApiResponse(code = 400, message = "Smth wrong"),

    })
    @GetMapping("/count/favorite")
    public ResponseEntity<Integer> getAllCountFavoriteByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return favoriteMoviesService.getAllCountByIdMovie(idMovie);
    }

}