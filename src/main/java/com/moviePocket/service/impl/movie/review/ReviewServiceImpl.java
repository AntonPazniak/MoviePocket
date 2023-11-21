package com.moviePocket.service.impl.movie.review;

import com.moviePocket.entities.movie.review.ParsReview;
import com.moviePocket.entities.movie.review.Review;
import com.moviePocket.entities.movie.review.ReviewMovie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.movie.review.LikeReviewRepository;
import com.moviePocket.repository.movie.review.ReviewMovieRepository;
import com.moviePocket.repository.movie.review.ReviewRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.movie.raview.MovieReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReviewServiceImpl implements MovieReviewService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private LikeReviewRepository likeReviewRepository;
    @Autowired
    private ReviewMovieRepository reviewMovieRepository;

    public ResponseEntity<Void> createMovieReview(String email, Long idMovie, String title, String content) {
        Review review = createReview(email, title, content);
        if (review == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            ReviewMovie reviewMovie = new ReviewMovie(idMovie, review);
            reviewMovieRepository.save(reviewMovie);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<Void> updateReview(Long idReview, String username, String title, String content) {
        User user = userRepository.findByEmail(username);
        Review movieReview = reviewRepository.getById(idReview);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (movieReview == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (movieReview.getUser() != user)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else {
            movieReview.setTitle(title);
            movieReview.setContent(content);
            reviewRepository.save(movieReview);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<Void> delReview(Long idReview, String username) {
        User user = userRepository.findByEmail(username);
        Review review = reviewRepository.getById(idReview);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!Objects.equals(review.getUser(), user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            ReviewMovie reviewMovie = reviewMovieRepository.findByReview(review);
            if (reviewMovie != null) {
                reviewMovieRepository.delete(reviewMovie);
                likeReviewRepository.deleteAllByReview(review);
                reviewRepository.delete(review);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private Review createReview(String email, String title, String content) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return null;
        else {
            Review review = new Review(user, title, content);
            reviewRepository.save(review);
            return review;
        }
    }

    public ResponseEntity<Boolean> authorshipCheck(Long idReview, String username) {
        try {
            User user = userRepository.findByEmail(username);
            Review review = reviewRepository.getById(idReview);
            return ResponseEntity.ok(review.getUser().equals(user));
        } catch (EntityNotFoundException e) {
            System.out.println(e);
        }
        return ResponseEntity.ok(false);
    }


    public ResponseEntity<ParsReview> getByIdReview(Long idReview) {
        try {
            Review review = reviewRepository.getById(idReview);
            return ResponseEntity.ok(new ParsReview(
                    review.getTitle(),
                    review.getContent(),
                    review.getUser().getUsername(),
                    review.getCreated(),
                    review.getUpdated(),
                    review.getId(),
                    new int[]{
                            likeReviewRepository.countByMovieReviewAndLickOrDisIsTrue(review),
                            likeReviewRepository.countByMovieReviewAndLickOrDisIsFalse(review)
                    }
            ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.ok().body(null);
        }
    }

    public ResponseEntity<List<ParsReview>> getAllByIDMovie(Long idMovie) {
        List<Review> movieList = reviewMovieRepository.findReviewsByMovieId(idMovie);
        if (movieList.isEmpty()) {
            List<ParsReview> reviewList = new ArrayList<>();
            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        }
        return ResponseEntity.ok(parsMovieReview(movieList));
    }

    public ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie) {
        return ResponseEntity.ok(reviewMovieRepository.countByMovieId(idMovie));
    }

    private List<ParsReview> parsMovieReview(List<Review> movieReviewList) {
        List<ParsReview> reviewList = new ArrayList<>();
        for (Review movieReview : movieReviewList) {
            reviewList.add(new ParsReview(
                    movieReview.getTitle(),
                    movieReview.getContent(),
                    movieReview.getUser().getUsername(),
                    movieReview.getCreated(),
                    movieReview.getUpdated(),
                    movieReview.getId(),
                    new int[]{
                            likeReviewRepository.countByMovieReviewAndLickOrDisIsTrue(movieReview),
                            likeReviewRepository.countByMovieReviewAndLickOrDisIsFalse(movieReview)
                    }
            ));
        }
        return reviewList;
    }

}
