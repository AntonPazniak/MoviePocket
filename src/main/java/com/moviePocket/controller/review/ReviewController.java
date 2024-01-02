package com.moviePocket.controller.review;


import com.moviePocket.entities.review.ParsReview;
import com.moviePocket.service.inter.raview.LikeMovieReviewService;
import com.moviePocket.service.inter.raview.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;
    @Autowired
    LikeMovieReviewService likeMovieReviewService;

    @PostMapping("/movie/set")
    public ResponseEntity<Void> setMovieReview(@RequestParam("idMovie") Long idMovie,
                                               @RequestParam("title") String title,
                                               @RequestBody String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return reviewService.createMovieReview(authentication.getName(), idMovie, title, content);
    }


    @PostMapping("/list/set")
    public ResponseEntity<Void> setListReview(@RequestParam("idList") Long idList,
                                              @RequestParam("title") String title,
                                              @RequestBody String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return reviewService.createListReview(authentication.getName(), idList, title, content);
    }

    @PostMapping("/post/set")
    public ResponseEntity<Void> setPostReview(@RequestParam("idPost") Long idPost,
                                              @RequestParam("title") String title,
                                              @RequestBody String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return reviewService.createPostReview(authentication.getName(), idPost, title, content);
    }

    @PostMapping("/up")
    public ResponseEntity<Void> setUpdateReview(@RequestParam("idReview") Long idReview,
                                                @RequestParam("title") String title,
                                                @RequestBody String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return reviewService.updateReview(idReview, authentication.getName(), title, content);
    }

    @GetMapping("/get")
    public ResponseEntity<ParsReview> getByIdReview(@RequestParam("idReview") Long idReview) {
        return reviewService.getByIdReview(idReview);
    }

    @GetMapping("/count/user")
    public ResponseEntity<Integer> allUserReviews() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return reviewService.getCountByUser(authentication.getName());
    }

    @GetMapping("/movie/all")
    public ResponseEntity<List<ParsReview>> getAllReviewByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return reviewService.getAllByIDMovie(idMovie);
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<ParsReview>> getAllReviewByIdList(@RequestParam("idList") Long idList) {
        return reviewService.getAllByIdList(idList);
    }

    @GetMapping("/post/all")
    public ResponseEntity<List<ParsReview>> getAllReviewByIdPost(@RequestParam("idPost") Long idPost) {
        return reviewService.getAllByIdPost(idPost);
    }

    @PostMapping("/del")
    public ResponseEntity<Void> delMovieReview(@RequestParam("idReview") Long idReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return reviewService.delReview(idReview, authentication.getName());
    }

    @PostMapping("/like")
    public ResponseEntity<Void> setLike(@RequestParam("idReview") Long idReview, @RequestParam("like") boolean like) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return likeMovieReviewService.setLikeOrDisOrDel(authentication.getName(), idReview, like);
    }

    @GetMapping("/like")
    public ResponseEntity<Boolean> getLikeOrDisByIdReview(@RequestParam("idReview") Long idReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return likeMovieReviewService.getLikeOrDis(authentication.getName(), idReview);
    }

    @GetMapping("/likes")
    public ResponseEntity<Integer[]> getAllLikeReviewByIdMovie(@RequestParam("idReview") Long idReview) {
        return likeMovieReviewService.getAllLikeAndDisByIdMovieReview(idReview);
    }

    @GetMapping("/authorship")
    public ResponseEntity<Boolean> getAuthorshipByIdMovie(@RequestParam("idReview") Long idReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return reviewService.authorshipCheck(idReview, authentication.getName());
    }
}
