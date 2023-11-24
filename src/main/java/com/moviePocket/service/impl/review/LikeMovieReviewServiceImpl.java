package com.moviePocket.service.impl.review;


import com.moviePocket.entities.review.LikeMovieReview;
import com.moviePocket.entities.review.Review;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.review.LikeReviewRepository;
import com.moviePocket.repository.review.ReviewRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.movie.raview.LikeMovieReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class LikeMovieReviewServiceImpl implements LikeMovieReviewService {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    LikeReviewRepository likeReviewRepository;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Void> setLikeOrDisOrDel(String username, Long id, boolean likeOrDis) {
        Review movieReview = reviewRepository.getById(id);
        User user = userRepository.findByEmail(username);
        LikeMovieReview likeMovieReview = likeReviewRepository.getByUserAndReview(user, movieReview);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        {
            if (likeMovieReview == null) {
                likeReviewRepository.save(new LikeMovieReview(movieReview, user, likeOrDis));
            } else if (likeMovieReview.isLickOrDis() == likeOrDis) {
                likeReviewRepository.delete(likeMovieReview);
            } else {
                likeMovieReview.setLickOrDis(likeOrDis);
                likeReviewRepository.save(likeMovieReview);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<Boolean> getLikeOrDis(String username, Long id) {
        Review movieReview = reviewRepository.getById(id);
        User user = userRepository.findByEmail(username);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (movieReview == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        LikeMovieReview likeMovieReview = likeReviewRepository.getByUserAndReview(user, movieReview);
        if (likeMovieReview == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(likeMovieReview.isLickOrDis());
    }

    public ResponseEntity<Integer[]> getAllLikeAndDisByIdMovieReview(Long idReview) {
        Review movieReview = reviewRepository.getById(idReview);
        if (movieReview == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        {
            return ResponseEntity.ok(new Integer[]{
                    likeReviewRepository.countByMovieReviewAndLickOrDisIsTrue(movieReview),
                    likeReviewRepository.countByMovieReviewAndLickOrDisIsFalse(movieReview)
            });
        }
    }
}
