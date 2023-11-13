package com.moviePocket.controller.movie.post;

import com.moviePocket.entities.movie.post.ParsPost;
import com.moviePocket.service.movie.post.LikePostService;
import com.moviePocket.service.movie.post.MovieListInPostService;
import com.moviePocket.service.movie.post.PostService;
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
@RequestMapping("/movies/post")
@Api(value = "Post Controller", description = "Controller related to the post manipulations")
public class PostController {

    private final PostService postService;

    private final MovieListInPostService movieListInPostService;

    private final LikePostService likePostService;

    @ApiOperation(value = "Create a new post", notes = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created new post"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated")
    })
    @PostMapping("/set")
    public ResponseEntity<?> setNewPost(@RequestParam("title") String title,
                                        @RequestParam("content") String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.setPost(authentication.getName(), title, content);
    }

    @ApiOperation(value = "Update post title", notes = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated title"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 403, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "Post not found")
    })
    @PostMapping("/updateTitle")
    public ResponseEntity<Void> setUpdatePostTitle(@RequestParam("idPost") Long idPost,
                                                   @RequestParam("title") String title) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.updatePostTitle(authentication.getName(), idPost, title);
    }

    @ApiOperation(value = "Update post content", notes = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated content"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 403, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "post not found")
    })
    @PostMapping("/updateContent")
    public ResponseEntity<Void> setUpdatePostContent(@RequestParam("idPost") Long idPost,
                                                     @RequestParam("content") String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.updatePostContent(authentication.getName(), idPost, content);
    }


    @ApiOperation(value = "Delete post and all that it had(movie lists in it and likes from other 2 tables", notes = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated title"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 403, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "Post not found")
    })
    @PostMapping("/del")
    public ResponseEntity<Void> delPost(@RequestParam("idPost") Long idPost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.deletePost(authentication.getName(), idPost);
    }


    @ApiOperation(value = "Get post", notes = "Returns a post for the given post ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved post"),
            @ApiResponse(code = 400, message = "Invalid post ID"),
            @ApiResponse(code = 404, message = "Post not found")
    })
    @GetMapping("/get")
    public ResponseEntity<List<ParsPost>> getPost(@RequestParam("idPost") Long idPost) {
        return postService.getPost(idPost);
    }

    @ApiOperation(value = "Get post by title", notes = "Returns a post that matches the title if it doesn't match it's empty post")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved post"),
    })
    @GetMapping("/getByTitle")
    public ResponseEntity<?> getPostByTitle(@RequestParam("title") String title) {
        return postService.getAllByTitle(title);
    }

    @ApiOperation(value = "Add or delete movie list from post", notes = "Adds or deletes a movie list from the specified post")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added or deleted movie to/from post"),
            @ApiResponse(code = 400, message = "Invalid post ID or movie ID"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "post or movie not found")
    })
    @PostMapping("/setMovieList")
    public ResponseEntity<Void> setOrDelMovieListInPost(@RequestParam("idPost") Long idPost, @RequestParam("idMovieList") Long idMovieList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieListInPostService.addOrDelMovieListFromPost(authentication.getName(), idPost, idMovieList);
    }

    @ApiOperation(value = "Like or dislike post", notes = "Likes or dislikes the specified post")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully liked or disliked post"),
            @ApiResponse(code = 400, message = "Invalid post ID"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "post not found")
    })
    @PostMapping("/setLike")
    public ResponseEntity<Void> setLikeOrDesPost(@RequestParam("idPost") Long idPost, @RequestParam("like") Boolean like) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return likePostService.setLikeOrDisOrDel(authentication.getName(), idPost, like);
    }

    @ApiOperation(value = "Get all post in the system", notes = "Returns a list of all posts")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all posts")
    })
    @GetMapping("/getAllPosts")
    public ResponseEntity<List<ParsPost>> getAllList() {
        return postService.getAllPosts();
    }

    @ApiOperation(value = "Get all my posts", notes = "Returns a list of all posts for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all posts for authenticated user"),
            @ApiResponse(code = 401, message = "User not authenticated")
    })
    @GetMapping("/getAllMyPosts")
    public ResponseEntity<List<ParsPost>> getAllMyLists() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.getAllMyPosts(authentication.getName());
    }

    @ApiOperation(value = "Get all posts of user", notes = "Returns a list of all posts for the specified username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all posts for specified username"),
            @ApiResponse(code = 400, message = "Invalid username"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @GetMapping("/getAllUserPosts")
    public ResponseEntity<List<ParsPost>> getAllUsername(@RequestParam("username") String username) {
        return postService.getAllByUsernamePosts(username);
    }
}
