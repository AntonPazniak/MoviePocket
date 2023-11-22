package com.moviePocket.service.impl.review;

import com.moviePocket.entities.movie.list.MovieList;
import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.review.*;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.movie.list.MovieListRepository;
import com.moviePocket.repository.post.PostRepository;
import com.moviePocket.repository.review.*;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.movie.raview.ReviewService;
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
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private LikeReviewRepository likeReviewRepository;
    @Autowired
    private ReviewMovieRepository reviewMovieRepository;
    @Autowired
    private MovieListRepository movieListRepository;
    @Autowired
    private ReviewListRepository reviewListRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReviewPostRepository reviewPostRepository;

    @Transactional
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

    @Transactional
    public ResponseEntity<Void> createListReview(String email, Long idList, String title, String content) {
        MovieList movieList = movieListRepository.getById(idList);
        if (movieList != null) {
            Review review = createReview(email, title, content);
            if (review == null)
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            else {
                ReviewList reviewList = new ReviewList(movieList, review);
                reviewListRepository.save(reviewList);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<Void> createPostReview(String email, Long idPost, String title, String content) {
        Post post = postRepository.getById(idPost);
        if (post != null) {
            Review review = createReview(email, title, content);
            if (review == null)
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            else {
                ReviewPost reviewPost = new ReviewPost(post, review);
                reviewPostRepository.save(reviewPost);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
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
            ReviewList reviewList = reviewListRepository.findByReview(review);
            if (reviewList != null) {
                reviewListRepository.delete(reviewList);
                likeReviewRepository.deleteAllByReview(review);
                reviewRepository.delete(review);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            ReviewPost reviewPost = reviewPostRepository.findByReview(review);
            if (reviewPost != null) {
                reviewPostRepository.delete(reviewPost);
                likeReviewRepository.deleteAllByReview(review);
                reviewRepository.delete(review);
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
        List<Review> reviews = reviewMovieRepository.findReviewsByMovieId(idMovie);
        if (reviews.isEmpty()) {
            List<ParsReview> reviewList = new ArrayList<>();
            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        }
        return ResponseEntity.ok(parsMovieReview(reviews));
    }

    public ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie) {
        return ResponseEntity.ok(reviewMovieRepository.countByIdMovie(idMovie));
    }

    public ResponseEntity<List<ParsReview>> getAllByIdList(Long idList) {
        MovieList movieList = movieListRepository.getById(idList);
        List<Review> reviews = reviewListRepository.findReviewsByMovieList(movieList);
        if (reviews.isEmpty()) {
            List<ParsReview> reviewList = new ArrayList<>();
            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        }
        return ResponseEntity.ok(parsMovieReview(reviews));
    }

    public ResponseEntity<Integer> getCountByIdList(Long idList) {
        return ResponseEntity.ok(reviewListRepository.countByMovieList_Id(idList));
    }

    public ResponseEntity<List<ParsReview>> getAllByIdPost(Long idPost) {
        Post post = postRepository.getById(idPost);
        List<Review> reviews = reviewPostRepository.findReviewsByPost(post);
        if (reviews.isEmpty()) {
            List<ParsReview> reviewList = new ArrayList<>();
            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        }
        return ResponseEntity.ok(parsMovieReview(reviews));
    }

    public ResponseEntity<Integer> getCountByIdPost(Long idList) {
        return ResponseEntity.ok(reviewPostRepository.countByPost_Id(idList));
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