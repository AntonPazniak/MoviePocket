package com.moviePocket.service.impl.admin;

import com.moviePocket.entities.review.Review;
import com.moviePocket.entities.review.ReviewList;
import com.moviePocket.entities.review.ReviewMovie;
import com.moviePocket.entities.review.ReviewPost;
import com.moviePocket.entities.user.BlockedUser;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.admin.BlockedUserRepository;
import com.moviePocket.repository.review.*;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.inter.admin.BlockedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockedUserServiceImpl implements BlockedUserService {

    private final BlockedUserRepository blockedUserRepository;
    private final UserRepository userRepository;
    private final ReviewListRepository reviewListRepository;
    private final ReviewPostRepository reviewPostRepository;
    private final ReviewMovieRepository reviewMovieRepository;
    private final ReviewRepository reviewRepository;
    private final LikeReviewRepository likeReviewRepository;


    public BlockedUser saveBlockedUser(BlockedUser blockedUser) {

        return blockedUserRepository.save(blockedUser);
    }

    public List<BlockedUser> getAllBlockedUsers() {
        return blockedUserRepository.findAll();
    }

    public BlockedUser findById(Long blockedUserId) {
        return blockedUserRepository.findById(blockedUserId).orElse(null);
    }

    @Override
    public void delete(BlockedUser blockedUser) {
        blockedUserRepository.delete(blockedUser);
    }

    @Transactional
    public ResponseEntity<Void> delAdminReview(Long idReview, String username) {
        User user = userRepository.findByEmail(username);
        Review review = reviewRepository.getById(idReview);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!user.getRoles().equals("ROLE_ADMIN")) {
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
}
