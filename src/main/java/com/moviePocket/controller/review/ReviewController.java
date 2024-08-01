/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.review;

import com.moviePocket.controller.dto.review.ReviewDTO;
import com.moviePocket.controller.dto.review.ReviewLikeDTO;
import com.moviePocket.service.inter.raview.LikeReviewService;
import com.moviePocket.service.inter.raview.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final LikeReviewService likeMovieReviewService;

    @Operation(summary = "Create or update a movie review",
            description = "Sets a review for a movie. The review is created or updated based on the provided ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created or updated the movie review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid parameters."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @PostMapping("/movie/set")
    public ResponseEntity<Object> setMovieReview(@RequestParam("idMovie") Long idMovie,
                                                 @RequestParam("title") String title,
                                                 @RequestBody String content) {
        reviewService.createReviewMovie(idMovie, title, content);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create or update a list review",
            description = "Sets a review for a list. The review is created or updated based on the provided ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created or updated the list review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid parameters."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @PostMapping("/list/set")
    public ResponseEntity<Object> setListReview(@RequestParam("idList") Long idList,
                                                @RequestParam("title") String title,
                                                @RequestBody String content) {
        reviewService.createReviewList(idList, title, content);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create or update a post review",
            description = "Sets a review for a post. The review is created or updated based on the provided ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created or updated the post review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid parameters."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @PostMapping("/post/set")
    public ResponseEntity<Object> setPostReview(@RequestParam("idPost") Long idPost,
                                                @RequestParam("title") String title,
                                                @RequestBody String content) {
        reviewService.createReviewPost(idPost, title, content);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update an existing review",
            description = "Updates an existing review based on the provided ID and new content.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid parameters."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @PatchMapping("/up")
    public ResponseEntity<Object> updateReview(@RequestParam("idReview") Long idReview,
                                               @RequestParam("title") String title,
                                               @RequestBody String content) {
        reviewService.updateReview(idReview, title, content);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get a review by ID",
            description = "Retrieves a review based on the provided ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid review ID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @GetMapping("/get")
    public ResponseEntity<ReviewDTO> getReviewById(@RequestParam("idReview") Long idReview) {
        return ResponseEntity.ok(reviewService.getReviewById(idReview));
    }

    @Operation(summary = "Get count of user's reviews",
            description = "Retrieves the count of reviews made by the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the count of user's reviews."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @GetMapping("/count/user")
    public ResponseEntity<Integer> getUserReviewCount() {
        return ResponseEntity.ok(reviewService.getCountReviewByUser());
    }

    @Operation(summary = "Get all reviews for a movie",
            description = "Retrieves all reviews for a specific movie based on the movie ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all reviews for the movie."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid movie ID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @GetMapping("/movie/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByMovieId(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(reviewService.getAllReviewByIdMovie(idMovie));
    }

    @Operation(summary = "Get all reviews for a list",
            description = "Retrieves all reviews for a specific list based on the list ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all reviews for the list."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid list ID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @GetMapping("/list/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByListId(@RequestParam("idList") Long idList) {
        return ResponseEntity.ok(reviewService.getAllReviewByIdList(idList));
    }

    @Operation(summary = "Get all reviews for a post",
            description = "Retrieves all reviews for a specific post based on the post ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all reviews for the post."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid post ID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @GetMapping("/post/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByPostId(@RequestParam("idPost") Long idPost) {
        return ResponseEntity.ok(reviewService.getAllReviewByIdPost(idPost));
    }

    @Operation(summary = "Delete a review",
            description = "Deletes a review based on the provided review ID.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid review ID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @DeleteMapping("/del")
    public ResponseEntity<Object> deleteReview(@RequestParam("idReview") Long idReview) {
        reviewService.deleteReview(idReview);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Set reaction on a review",
            description = "Sets a reaction (like or dislike) for the specified review. Use `like=true` to set a like, or `like=false` to set a dislike.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set or updated the reaction on the review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid review ID or like parameter. Ensure `like` is either `true` or `false`."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @PostMapping("/like")
    public ResponseEntity<Object> setReaction(@RequestParam("idReview") Long idReview, @RequestParam("like") boolean like) {
        likeMovieReviewService.setLikeOrDisLike(idReview, like);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a reaction on a review",
            description = "Deletes the reaction (like or dislike) on the specified review.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the reaction from the review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid review ID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @DeleteMapping("/like/del")
    public ResponseEntity<Object> deleteReaction(@RequestParam("idReview") Long idReview) {
        likeMovieReviewService.deleteReaction(idReview);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get reaction on a review",
            description = "Retrieves the reaction (like or dislike) for the specified review.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the reaction on the review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid review ID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @GetMapping("/like")
    public ResponseEntity<Boolean> getReaction(@RequestParam("idReview") Long idReview) {
        return ResponseEntity.ok(likeMovieReviewService.getReaction(idReview));
    }

    @Operation(summary = "Get all reactions for a review",
            description = "Retrieves all reactions (likes and dislikes) for the specified review.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all reactions for the review."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid review ID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @GetMapping("/likes")
    public ResponseEntity<ReviewLikeDTO> getAllLikesByReviewId(@RequestParam("idReview") Long idReview) {
        return ResponseEntity.ok(likeMovieReviewService.getAllReactionReview(idReview));
    }

    @Operation(summary = "Get authorship of a review",
            description = "Checks if the authenticated user is the author of the specified review.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved authorship information."),
            @ApiResponse(responseCode = "400", description = "Bad request. Invalid review ID."),
            @ApiResponse(responseCode = "401", description = "Unauthorized. The user is not authenticated.")
    })
    @GetMapping("/authorship")
    public ResponseEntity<Boolean> getAuthorship(@RequestParam("idReview") Long idReview) {
        return ResponseEntity.ok(reviewService.getAuthorship(idReview));
    }
}
