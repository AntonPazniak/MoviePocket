/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.post;

import com.moviePocket.entities.post.ParsPost;
import com.moviePocket.service.inter.post.LikePostService;
import com.moviePocket.service.inter.post.PostService;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.updatePost(authentication.getName(), idPost, title, content);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.deletePost(authentication.getName(), idPost);
    }

    @Operation(summary = "Get post", description = "Returns a post for the given post ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
            @ApiResponse(responseCode = "400", description = "Invalid post ID"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/get")
    public ResponseEntity<ParsPost> getPost(@RequestParam("idPost") Long idPost) {
        return postService.getPost(idPost);
    }

    @GetMapping("/movie")
    public ResponseEntity<List<ParsPost>> getAllPostsByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return postService.getAllByIdMovie(idMovie);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ParsPost>> getAllPostsByIdList(@RequestParam("idList") Long idList) {
        return postService.getAllByIdList(idList);
    }

    @GetMapping("/person")
    public ResponseEntity<List<ParsPost>> getAllPostsByIdPerson(@RequestParam("idPerson") Long idPerson) {
        return postService.getAllByIdPerson(idPerson);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ParsPost>> getAllMyPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.getAllByUser(authentication.getName());
    }

    @Operation(summary = "Get post by title", description = "Returns a post that matches the title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
    })
    @GetMapping("/get/title")
    public ResponseEntity<?> getPostByPartialTitle(@RequestParam("title") String title) {
        return postService.getAllByPartialTitle(title);
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return likePostService.setLikeOrDisOrDel(authentication.getName(), idPost, like);
    }

    @Operation(summary = "Get boolean true/false if you are post author", description = "Returns boolean true if you are")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "You are author of the post"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/authorship")
    public ResponseEntity<Boolean> getAuthorshipByIdPost(@RequestParam("idPost") Long idPost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return postService.authorshipCheck(idPost, authentication.getName());
    }

    @Operation(summary = "Get all posts of user", description = "Returns a list of all posts for the specified username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all posts for specified username"),
            @ApiResponse(responseCode = "401", description = "Invalid username")
    })
    @GetMapping("/someUser")
    public ResponseEntity<List<ParsPost>> getAllByUsername(@RequestParam("username") String username) {
        return postService.getAllByUsernamePosts(username);
    }

    @Operation(summary = "Get num of likes by post", description = "Returns list of Integers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved num of likes"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/like")
    public ResponseEntity<Boolean> getAllLikePostsByIdMovie(@RequestParam("idPost") Long idPost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return likePostService.getLikeOrDis(authentication.getName(), idPost);
    }

    @GetMapping("/get/last")
    public ResponseEntity<List<ParsPost>> getLast() {
        return postService.getTop10LatestPosts();
    }

    @GetMapping("/get/top")
    public ResponseEntity<List<ParsPost>> getTop() {
        return postService.getTop10LikedPosts();
    }
}
