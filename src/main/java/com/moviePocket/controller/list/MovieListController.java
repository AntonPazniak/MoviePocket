package com.moviePocket.controller.list;


import com.moviePocket.entities.list.ParsList;
import com.moviePocket.service.inter.list.CategoriesMovieListService;
import com.moviePocket.service.inter.list.LikeListService;
import com.moviePocket.service.inter.list.MovieInListService;
import com.moviePocket.service.inter.list.MovieListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies/list")
@Api(value = "Movie List Controller", description = "Controller related to the movie lists manipulations")
public class MovieListController {


    private final MovieListService movieListService;

    private final MovieInListService movieInListService;

    private final LikeListService likeListService;

    private final CategoriesMovieListService categoriesMovieListService;

    @ApiOperation(value = "Create a new movie list", notes = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created new movie list"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated")
    })
    @PostMapping("/set")
    public ResponseEntity<?> setNewMovieList(@RequestParam("title") String title,
                                             @RequestParam("content") String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieListService.setList(authentication.getName(), title, content);
    }

    @ApiOperation(value = "Update movie list title", notes = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated title"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 403, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "Movie list not found")
    })
    @PostMapping("/up")
    public ResponseEntity<Void> setUpdateMovieListTitle(@RequestParam("idMovieList") Long idMovieList,
                                                        @RequestParam("title") String title,
                                                        @RequestParam("content") String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieListService.updateList(authentication.getName(), idMovieList, title, content);
    }

    @ApiOperation(value = "Delete movie list and all that it had(movies in it adn likes from other 2 tables", notes = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated title"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 403, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "Movie list not found")
    })
    @PostMapping("/del")
    public ResponseEntity<Void> delMovieList(@RequestParam("idMovieList") Long idMovieList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieListService.deleteList(authentication.getName(), idMovieList);
    }


    @ApiOperation(value = "Get movie list", notes = "Returns a list of movies for the given movie list ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved movie list"),
            @ApiResponse(code = 400, message = "Invalid movie list ID"),
            @ApiResponse(code = 404, message = "Movie list not found")
    })
    @GetMapping("/get")
    public ResponseEntity<ParsList> getMovieList(@RequestParam("idMovieList") Long idMovieList) {
        return movieListService.getList(idMovieList);
    }

    @ApiOperation(value = "Get movie list by title", notes = "Returns a list of movies that matches the title if it doesn't match it's empty list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved movie list"),
    })
    @GetMapping("/getByTitle")
    public ResponseEntity<?> getMovieListByTitle(@RequestParam("idMovieList") String title) {
        return movieListService.getAllByTitle(title);
    }

    @ApiOperation(value = "Add or delete movie from list", notes = "Adds or deletes a movie from the specified movie list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added or deleted movie from list"),
            @ApiResponse(code = 400, message = "Invalid movie list ID or movie ID"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "Movie list or movie not found")
    })
    @PostMapping("/setMovie")
    public ResponseEntity<Void> setOrDelMovieInMovieList(@RequestParam("idList") Long idList, @RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieInListService.addOrDelMovieFromList(authentication.getName(), idList, idMovie);
    }

    @ApiOperation(value = "Like or dislike movie list", notes = "Likes or dislikes the specified movie list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully liked or disliked movie list"),
            @ApiResponse(code = 400, message = "Invalid movie list ID"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "Movie list not found")
    })
    @PostMapping("/setLike")
    public ResponseEntity<Void> setLikeOrDesMovieList(@RequestParam("idList") Long idList, @RequestParam("like") Boolean like) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return likeListService.setLikeOrDisOrDel(authentication.getName(), idList, like);
    }

    @ApiOperation(value = "Set or delete category(tag)", notes = "Sets  or delete the category for the specified movie list that it can be searched by after")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully set category for movie list"),
            @ApiResponse(code = 400, message = "Invalid movie list ID or category ID"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "Movie list or category not found")
    })
    @PostMapping("/setOrDeleteGenre")
    public ResponseEntity<?> setOrDelCategoryMovieList(@RequestParam("idList") Long idList, @RequestParam("idCategory") Long idCategory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return categoriesMovieListService.setOrDelCategoryList(authentication.getName(), idList, idCategory);
    }

    @ApiOperation(value = "Get all my movie lists", notes = "Returns a list of all movie lists for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all movie lists for authenticated user"),
            @ApiResponse(code = 401, message = "User not authenticated")
    })
    @GetMapping("/getAllMyLists")
    public ResponseEntity<List<ParsList>> getAllMyLists() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieListService.getAllMyList(authentication.getName());
    }

    @ApiOperation(value = "Get all movie lists of user", notes = "Returns a list of all movie lists for the specified username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all movie lists for specified username"),
            @ApiResponse(code = 400, message = "Invalid username"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @GetMapping("/getAllUserLists")
    public ResponseEntity<List<ParsList>> getAllUsername(@RequestParam("username") String username) {
        return movieListService.getAllByUsernameList(username);
    }
    @ApiOperation(value = "Get num of likes by movie list", notes = "Returns list of Integers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved num of likes "),
            @ApiResponse(code = 404, message = "User not found")
    })

    @GetMapping("/getAllLikes")
    public ResponseEntity<Integer[]> getAllLikePostsByIdMovie(@RequestParam("idMovieList") Long idMovieList) {
        return likeListService.getAllLikeAndDisByIdMovieList(idMovieList);
    }

    @GetMapping("/getAllListsContainingMovie")
    public ResponseEntity<List<ParsList>> getAllListsContainingMovie(@RequestParam("idMovie") Long idMovie) {
        return movieListService.getAllListsContainingMovie(idMovie);
    }


}
