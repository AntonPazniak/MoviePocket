package com.moviePocket.controller.movie.review;


import com.moviePocket.entities.movie.review.ParsReview;
import com.moviePocket.service.movie.raview.LikeMovieReviewService;
import com.moviePocket.service.movie.raview.MovieReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/review")
@Controller
public class ReviewMovieController {

    @Autowired
    MovieReviewService movieReviewService;
    @Autowired
    LikeMovieReviewService likeMovieReviewService;

    @PostMapping("/set")
    public ResponseEntity<Void> setMovieReview(@RequestParam("idMovie") Long idMovie,
                                               @RequestParam("title") String title,
                                               @RequestParam("content") String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieReviewService.createMovieReview(authentication.getName(), idMovie, title, content);
    }

    @PostMapping("/up")
    public ResponseEntity<Void> setUpdateReview(@RequestParam("idReview") Long idReview,
                                                     @RequestParam("title") String title,
                                                     @RequestParam("content") String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieReviewService.updateReview(idReview, authentication.getName(), title, content);
    }

    @GetMapping("/get")
    public ResponseEntity<ParsReview> getByIdReview(@RequestParam("idReview") Long idReview) {
        return movieReviewService.getByIdReview(idReview);
    }

    @GetMapping("/getAllByMovie")
    public ResponseEntity<List<ParsReview>> getAllReviewByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return movieReviewService.getAllByIDMovie(idMovie);
    }

    @PostMapping("/del")
    public ResponseEntity<Void> delMovieReview(@RequestParam("idReview") Long idReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieReviewService.delReview(idReview, authentication.getName());
    }

    @PostMapping("/setLike")
    public ResponseEntity<Void> setLike(@RequestParam("idReview") Long idReview, @RequestParam("like") boolean like) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return likeMovieReviewService.setLikeOrDisOrDel(authentication.getName(), idReview, like);
    }

    @GetMapping("/getLike")
    public ResponseEntity<Boolean> getLikeOrDisByIdReview(@RequestParam("idReview") Long idReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return likeMovieReviewService.getLikeOrDis(authentication.getName(), idReview);
    }

    @GetMapping("/getAllLike")
    public ResponseEntity<Integer[]> getAllLikeReviewByIdMovie(@RequestParam("idReview") Long idReview) {
        return likeMovieReviewService.getAllLikeAndDisByIdMovieReview(idReview);
    }

    @GetMapping("/getAuthorship")
    public ResponseEntity<Boolean> getAuthorshipByIdMovie(@RequestParam("idReview") Long idReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return movieReviewService.authorshipCheck(idReview, authentication.getName());
    }
}
