package com.moviePocket.controller.post;

import com.moviePocket.entities.post.ParsPost;
import com.moviePocket.service.inter.post.LikePostService;
import com.moviePocket.service.inter.post.PostService;
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
@RequestMapping("/post")
@Api(value = "Post Controller", description = "Controller related to the post manipulations")
public class PostController {

    private final PostService postService;

    private final LikePostService likePostService;

    @ApiOperation(value = "Create a new post", notes = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created new post"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated")
    })
    @PostMapping("/list/set")
    public ResponseEntity<ParsPost> setNewPostList(@RequestParam("title") String title,
                                                   @RequestParam("idList") Long idList,
                                                   @RequestBody String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.createPostList(authentication.getName(), title, content, idList);
    }

    @PostMapping("/movie/set")
    public ResponseEntity<ParsPost> setNewPostMovie(@RequestParam("title") String title,
                                             @RequestBody String content,
                                             @RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.createPostMovie(authentication.getName(), title, content, idMovie);
    }

    @PostMapping("/person/set")
    public ResponseEntity<ParsPost> setNewPostPerson(@RequestParam("title") String title,
                                              @RequestBody String content,
                                              @RequestParam("idPerson") Long idPerson) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.createPostPerson(authentication.getName(), title, content, idPerson);
    }


    @ApiOperation(value = "Update post title", notes = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated title"),
            @ApiResponse(code = 401, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 403, message = "Forbidden - user is not authenticated"),
            @ApiResponse(code = 404, message = "Post not found")
    })
    @PostMapping("/up")
    public ResponseEntity<Void> setUpdatePostTitle(@RequestParam("idPost") Long idPost,
                                                   @RequestParam("title") String title,
                                                   @RequestBody String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.updatePost(authentication.getName(), idPost, title, content);
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
    public ResponseEntity<ParsPost> getPost(@RequestParam("idPost") Long idPost) {
        return postService.getPost(idPost);
    }

    @GetMapping("/movie")
    public ResponseEntity<List<ParsPost>> getAllPostByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return postService.getAllByIdMovie(idMovie);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ParsPost>> getAllPostByIdList(@RequestParam("idList") Long idList) {
        return postService.getAllByIdList(idList);
    }

    @GetMapping("/person")
    public ResponseEntity<List<ParsPost>> getAllPostByIdPerson(@RequestParam("idPerson") Long idPerson) {
        return postService.getAllByIdPerson(idPerson);
    }


    //    @ApiOperation(value = "Get post by title", notes = "Returns a post that matches the title if it doesn't match it's empty post")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved post"),
//    })
//    @GetMapping("/getByTitle")
//    public ResponseEntity<?> getPostByTitle(@RequestParam("title") String title) {
//        return postService.getAllByTitle(title);
//    }
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

    @ApiOperation(value = "Get boolean true/false if you are post author", notes = "Returns bolean true if you are")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "You are author of the post "),
            @ApiResponse(code = 404, message = "User not found")
    })
    @GetMapping("/authorship")
    public ResponseEntity<Boolean> getAuthorshipByIdPost(@RequestParam("idPost") Long idPost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.authorshipCheck(idPost, authentication.getName());
    }

//    @ApiOperation(value = "Get all my posts", notes = "Returns a list of all posts for the authenticated user")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved all posts for authenticated user"),
//            @ApiResponse(code = 401, message = "User not authenticated")
//    })
//    @GetMapping("/getAllMyPosts")
//    public ResponseEntity<List<ParsPost>> getAllMyPosts() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return postService.getAllMyPosts(authentication.getName());
//    }
//
//    @ApiOperation(value = "Get all posts of user", notes = "Returns a list of all posts for the specified username")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved all posts for specified username"),
//            @ApiResponse(code = 400, message = "Invalid username"),
//            @ApiResponse(code = 404, message = "User not found")
//    })
//    @GetMapping("/getAllUserPosts")
//    public ResponseEntity<List<ParsPost>> getAllUsername(@RequestParam("username") String username) {
//        return postService.getAllByUsernamePosts(username);
//    }
//
//    @ApiOperation(value = "Get the most recent posts", notes = "Returns a sorted list of posts from newest to oldest")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved all posts "),
//            @ApiResponse(code = 404, message = "Not found")
//    })
//    @GetMapping("/getNewestPosts")
//    public ResponseEntity<List<ParsPost>> getNewestPosts() {
//        return postService.getNewestPosts();
//    }
//
//    @ApiOperation(value = "Get the oldest posts", notes = "Returns a sorted list of posts from oldest")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved all posts "),
//            @ApiResponse(code = 404, message = "Not found")
//    })
//    @GetMapping("/getOldestPosts")
//    public ResponseEntity<List<ParsPost>> getOldestPosts() {
//        return postService.getOldestPosts();
//    }
//
@ApiOperation(value = "Get num of likes by post", notes = "Returns list of Integers")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved num of likes "),
        @ApiResponse(code = 404, message = "User not found")
})
@GetMapping("/like")
public ResponseEntity<Boolean> getAllLikePostsByIdMovie(@RequestParam("idPost") Long idPost) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return likePostService.getLikeOrDis(authentication.getName(), idPost);
}
//
//    @ApiOperation(value = "Get the most liked(popular) posts", notes = "Returns a sorted list of posts from most liked to least")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved all posts "),
//            @ApiResponse(code = 404, message = "Not found")
//    })
//    @GetMapping("/getMostLikedPosts")
//    public ResponseEntity<List<ParsPost>> getMostLikedPosts() {
//        return likePostService.getMostLikedPosts();
//    }
//
//    @ApiOperation(value = "Get the least liked(popular) posts", notes = "Returns a sorted list of posts from most least to liked")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved all posts "),
//            @ApiResponse(code = 404, message = "Not found")
//    })
//    @GetMapping("/getLeastLikedPosts")
//    public ResponseEntity<List<ParsPost>> getLeastLikedPosts() {
//        return likePostService.getLeastLikedPosts();
//    }

    @GetMapping("/get/last")
    public ResponseEntity<List<ParsPost>> getLast() {
        return postService.getTop10LatestPosts();
    }

    @GetMapping("/get/top")
    public ResponseEntity<List<ParsPost>> getTop() {
        return postService.getTop10LikedPosts();
    }
}
