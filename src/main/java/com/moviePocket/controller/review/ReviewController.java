/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.review;


import com.moviePocket.controller.dto.review.ReviewLikeDTO;
import com.moviePocket.db.entities.review.ReviewDTO;
import com.moviePocket.service.inter.raview.LikeReviewService;
import com.moviePocket.service.inter.raview.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final LikeReviewService likeMovieReviewService;

    @PostMapping("/movie/set")
    public ResponseEntity<Object> setMovieReview(@RequestParam("idMovie") Long idMovie,
                                                 @RequestParam("title") String title,
                                                 @RequestBody String content) {
        reviewService.createReviewMovie(idMovie, title, content);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/list/set")
    public ResponseEntity<Object> setListReview(@RequestParam("idList") Long idList,
                                              @RequestParam("title") String title,
                                              @RequestBody String content) {
        reviewService.createReviewList(idList, title, content);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post/set")
    public ResponseEntity<Object> setPostReview(@RequestParam("idPost") Long idPost,
                                              @RequestParam("title") String title,
                                              @RequestBody String content) {
        reviewService.createReviewPost(idPost, title, content);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/up")
    public ResponseEntity<Object> setUpdateReview(@RequestParam("idReview") Long idReview,
                                                @RequestParam("title") String title,
                                                @RequestBody String content) {
        reviewService.updateReview(idReview, title, content);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<ReviewDTO> getByIdReview(@RequestParam("idReview") Long idReview) {
        return ResponseEntity.ok(reviewService.getReviewById(idReview));
    }

    @GetMapping("/count/user")
    public ResponseEntity<Integer> allUserReviews() {
        return ResponseEntity.ok(reviewService.getCountReviewByUser());
    }

    @GetMapping("/movie/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviewByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(reviewService.getAllReviewByIdMovie(idMovie));
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviewByIdList(@RequestParam("idList") Long idList) {
        return ResponseEntity.ok(reviewService.getAllReviewByIdList(idList));
    }

    @GetMapping("/post/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviewByIdPost(@RequestParam("idPost") Long idPost) {
        return ResponseEntity.ok(reviewService.getAllReviewByIdPost(idPost));
    }

    @DeleteMapping("/del")
    public ResponseEntity<Object> delMovieReview(@RequestParam("idReview") Long idReview) {
        reviewService.deleteReview(idReview);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/like")
    public ResponseEntity<Object> setReaction(@RequestParam("idReview") Long idReview, @RequestParam("like") boolean like) {
        likeMovieReviewService.setLikeOrDisLike(idReview, like);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/like/del")
    public ResponseEntity<Object> delReaction(@RequestParam("idReview") Long idReview) {
        likeMovieReviewService.deleteReaction(idReview);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/like")
    public ResponseEntity<Boolean> getReactionByIdReview(@RequestParam("idReview") Long idReview) {
        return ResponseEntity.ok(likeMovieReviewService.getReaction(idReview));
    }

    @GetMapping("/likes")
    public ResponseEntity<ReviewLikeDTO> getAllLikeReviewByIdMovie(@RequestParam("idReview") Long idReview) {
        return ResponseEntity.ok(likeMovieReviewService.getAllReactionReview(idReview));
    }

    @GetMapping("/authorship")
    public ResponseEntity<Boolean> getAuthorshipByIdMovie(@RequestParam("idReview") Long idReview) {
        return ResponseEntity.ok(reviewService.getAuthorship(idReview));
    }

}
