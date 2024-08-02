/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.list;

import com.moviePocket.controller.dto.list.ListDTO;
import com.moviePocket.db.entities.movie.Genre;
import com.moviePocket.service.inter.list.CategoriesMovieListService;
import com.moviePocket.service.inter.list.ListReactionService;
import com.moviePocket.service.inter.list.MovieListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies/list")
@Tag(name = "Movie List Controller", description = "Controller related to the movie lists manipulations")
public class MovieListController {

    private final MovieListService movieListService;
    private final ListReactionService likeListService;
    private final CategoriesMovieListService categoriesMovieListService;

    @Operation(summary = "Create a new movie list", description = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created new movie list"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated")
    })
    @PostMapping("/set")
    public ResponseEntity<ListDTO> setNewMovieList(@RequestParam("title") String title,
                                                   @RequestBody String content) {
        return ResponseEntity.ok(movieListService.setList(title, content));
    }

    @Operation(summary = "Update movie list title", description = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated title"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Movie list not found")
    })
    @PostMapping("/up")
    public ResponseEntity<ListDTO> setUpdateMovieListTitle(@RequestParam("idMovieList") Long idMovieList,
                                                        @RequestParam("title") String title,
                                                        @RequestBody String content) {
        return ResponseEntity.ok(movieListService.updateList(idMovieList, title, content));
    }

    @Operation(summary = "Delete movie list and all that it had(movies in it and likes from other 2 tables)", description = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated title"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Movie list not found")
    })
    @PostMapping("/del")
    public ResponseEntity<Object> delMovieList(@RequestParam("idMovieList") Long idMovieList) {
        movieListService.deleteList(idMovieList);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Return boolean saying whether movie is already in list or not", description = "Return Boolean true if it is")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie is in list"),
            @ApiResponse(responseCode = "404", description = "List is not found")
    })
    @GetMapping("/isInList")
    public ResponseEntity<Boolean> isMovieInList(@RequestParam("idList") Long idList, @RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(movieListService.isMovieInList(idList, idMovie));
    }

    @Operation(summary = "Get movie list", description = "Returns a list of movies for the given movie list ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved movie list"),
            @ApiResponse(responseCode = "400", description = "Invalid movie list ID"),
            @ApiResponse(responseCode = "404", description = "Movie list not found")
    })
    @GetMapping("/get")
    public ResponseEntity<ListDTO> getMovieList(@RequestParam("idMovieList") Long idMovieList) {
        return ResponseEntity.ok(movieListService.getList(idMovieList));
    }

    @Operation(summary = "Get movie list by partial title", description = "Returns a list of movies that matches the title if it doesn't match it's empty list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved movie list")
    })
    @GetMapping("/get/title")
    public ResponseEntity<List<ListDTO>> getMovieListByPartialTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(movieListService.getAllByPartialTitle(title));
    }

    @Operation(summary = "Add or delete movie from list", description = "Adds or deletes a movie from the specified movie list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added or deleted movie from list"),
            @ApiResponse(responseCode = "400", description = "Invalid movie list ID or movie ID"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Movie list or movie not found")
    })
    @PostMapping("/movie/set")
    public ResponseEntity<Object> setOrDelMovieInMovieList(@RequestParam("idList") Long idList, @RequestParam("idMovie") Long idMovie) {
        movieListService.addOrDelItemList(idList, idMovie);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Like or dislike movie list", description = "Likes or dislikes the specified movie list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully liked or disliked movie list"),
            @ApiResponse(responseCode = "400", description = "Invalid movie list ID"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Movie list not found")
    })
    @PostMapping("/like/set")
    public ResponseEntity<Void> setLikeOrDesMovieList(@RequestParam("idList") Long idList, @RequestParam("like") Boolean like) {
        likeListService.setLikeOrDisLike(idList, like);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/like/get")
    public ResponseEntity<Boolean> setLikeOrDesMovieList(@RequestParam("idList") Long idList) {
        return ResponseEntity.ok(likeListService.getReaction(idList));
    }

    @Operation(summary = "Set or delete category(tag)", description = "Sets or delete the category for the specified movie list that it can be searched by after")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set category for movie list"),
            @ApiResponse(responseCode = "400", description = "Invalid movie list ID or category ID"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Movie list or category not found")
    })
    @PostMapping("/genre/set")
    public ResponseEntity<Object> setOrDelCategoryMovieList(@RequestParam("idList") Long idList, @RequestParam("idCategory") Long idCategory) {
        categoriesMovieListService.setOrDelCategoryList(idList, idCategory);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all my movie lists", description = "Returns a list of all movie lists for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all movie lists for authenticated user"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/user/my")
    public ResponseEntity<List<ListDTO>> getAllMyLists() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(movieListService.getAllUserList());
    }

    @Operation(summary = "Get all movie lists of user", description = "Returns a list of all movie lists for the specified username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all movie lists for specified username"),
            @ApiResponse(responseCode = "400", description = "Invalid username"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/all")
    public ResponseEntity<List<ListDTO>> getAllUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(movieListService.getAllByUsernameList(username));
    }

    @GetMapping("/movie/containing")
    public ResponseEntity<List<ListDTO>> getAllListsContainingMovie(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(movieListService.getAllListsContainingMovie(idMovie));
    }

    @Operation(summary = "Get boolean true/false if you are list author", description = "Returns boolean true if you are")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "You are author of the list"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/authorship")
    public ResponseEntity<Boolean> getAuthorshipByIdMovie(@RequestParam("idList") Long idList) {
        return ResponseEntity.ok(movieListService.authorshipCheck(idList));
    }

    @GetMapping("/genre/all")
    public ResponseEntity<List<Genre>> getAllGenre() {
        return ResponseEntity.ok(categoriesMovieListService.getAll());
    }

    @GetMapping("/get/last")
    public ResponseEntity<List<ListDTO>> getLast() {
        return ResponseEntity.ok(movieListService.getTop10LatestLists());
    }

    @GetMapping("/get/top")
    public ResponseEntity<List<ListDTO>> getTop() {
        return ResponseEntity.ok(movieListService.getTop10LikedLists());
    }

}
