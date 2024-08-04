/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.post;

import com.moviePocket.controller.dto.post.PostDTO;
import com.moviePocket.service.inter.post.LikePostService;
import com.moviePocket.service.inter.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(name = "Post Controller", description = "Controller related to the post manipulations")
public class PostController {

    private final PostService postService;
    private final LikePostService likePostService;

    @Operation(summary = "Create a new post", description = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created new post"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated")
    })
    @PostMapping("/list/set")
    public ResponseEntity<PostDTO> setNewPostList(@RequestParam("title") String title,
                                                  @RequestParam("idList") Long idList,
                                                  @RequestBody String content) {
        return ResponseEntity.ok(postService.createPostList(title, content, idList));
    }

    @PostMapping("/movie/set")
    public ResponseEntity<PostDTO> setNewPostMovie(@RequestParam("title") String title,
                                                   @RequestBody String content,
                                                   @RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(postService.createPostMovie(title, content, idMovie));
    }

    @PostMapping("/person/set")
    public ResponseEntity<PostDTO> setNewPostPerson(@RequestParam("title") String title,
                                                    @RequestBody String content,
                                                    @RequestParam("idPerson") Long idPerson) {
        return ResponseEntity.ok(postService.createPostPerson(title, content, idPerson));
    }

    @Operation(summary = "Update post title", description = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated title"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/up")
    public ResponseEntity<Void> setUpdatePostTitle(@RequestParam("idPost") Long idPost,
                                                   @RequestParam("title") String title,
                                                   @RequestBody String content) {
        postService.updatePost(idPost, title, content);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete post and all that it had (movie lists in it and likes from other 2 tables)", description = "Return Http response Ok")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted post"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/del")
    public ResponseEntity<Void> delPost(@RequestParam("idPost") Long idPost) {
        postService.deletePost(idPost);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get post", description = "Returns a post for the given post ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
            @ApiResponse(responseCode = "400", description = "Invalid post ID"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/get")
    public ResponseEntity<PostDTO> getPost(@RequestParam("idPost") Long idPost) {
        return ResponseEntity.ok(postService.getPost(idPost));
    }

    @GetMapping("/movie")
    public ResponseEntity<List<PostDTO>> getAllPostsByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(postService.getAllByIdMovie(idMovie));
    }

    @GetMapping("/list")
    public ResponseEntity<List<PostDTO>> getAllPostsByIdList(@RequestParam("idList") Long idList) {
        return ResponseEntity.ok(postService.getAllByIdList(idList));
    }

    @GetMapping("/person")
    public ResponseEntity<List<PostDTO>> getAllPostsByIdPerson(@RequestParam("idPerson") Long idPerson) {
        return ResponseEntity.ok(postService.getAllByIdPerson(idPerson));
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostDTO>> getAllMyPosts() {
        return ResponseEntity.ok(postService.getAllByUser());
    }

    @Operation(summary = "Get post by title", description = "Returns a post that matches the title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
    })
    @GetMapping("/get/title")
    public ResponseEntity<List<PostDTO>> getPostByPartialTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(postService.getAllByPartialTitle(title));
    }

    @Operation(summary = "Like or dislike post", description = "Likes or dislikes the specified post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully liked or disliked post"),
            @ApiResponse(responseCode = "400", description = "Invalid post ID"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not authenticated"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping("/setLike")
    public ResponseEntity<Void> setLikeOrDesPost(@RequestParam("idPost") Long idPost, @RequestParam("like") Boolean like) {
        likePostService.setLikeOrDis(idPost, like);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get boolean true/false if you are post author", description = "Returns boolean true if you are")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "You are author of the post"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/authorship")
    public ResponseEntity<Boolean> getAuthorshipByIdPost(@RequestParam("idPost") Long idPost) {
        return ResponseEntity.ok(postService.authorshipCheck(idPost));
    }

    @Operation(summary = "Get all posts of user", description = "Returns a list of all posts for the specified username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all posts for specified username"),
            @ApiResponse(responseCode = "401", description = "Invalid username")
    })
    @GetMapping("/someUser")
    public ResponseEntity<List<PostDTO>> getAllByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(postService.getAllByUsernamePosts(username));
    }

    @Operation(summary = "Get num of likes by post", description = "Returns list of Integers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved num of likes"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/like")
    public ResponseEntity<Boolean> getAllLikePostsByIdMovie(@RequestParam("idPost") Long idPost) {
        return ResponseEntity.ok(likePostService.getReaction(idPost));
    }

    @GetMapping("/get/last")
    public ResponseEntity<List<PostDTO>> getLast() {
        return ResponseEntity.ok(postService.getTop10LatestPosts());
    }

    @GetMapping("/get/top")
    public ResponseEntity<List<PostDTO>> getTop() {
        return ResponseEntity.ok(postService.getTop10LikedPosts());
    }
}
