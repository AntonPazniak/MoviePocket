package com.moviePocket.service.impl.admin;

import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.post.PostList;
import com.moviePocket.entities.post.PostMovie;
import com.moviePocket.entities.post.PostPerson;
import com.moviePocket.entities.review.Review;
import com.moviePocket.entities.review.ReviewList;
import com.moviePocket.entities.review.ReviewMovie;
import com.moviePocket.entities.review.ReviewPost;
import com.moviePocket.entities.user.BlockedUser;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.admin.BlockedUserRepository;
import com.moviePocket.repository.post.PostListRepository;
import com.moviePocket.repository.post.PostMovieRepository;
import com.moviePocket.repository.post.PostPersonRepository;
import com.moviePocket.repository.post.PostRepository;
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
    private final PostRepository postRepository;
    private final PostListRepository postListRepository;
    private final PostMovieRepository postMovieRepository;
    private final PostPersonRepository postPersonRepository;


    public BlockedUser saveBlockedUser(BlockedUser blockedUser) {

        return blockedUserRepository.save(blockedUser);
    }

    public List<BlockedUser> getAllBlockedUsers() {
        return blockedUserRepository.findAll();
    }

    public BlockedUser findById(Long userId) {
        return blockedUserRepository.findByUser_Id(userId);
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
        }
        if (user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
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

    @Override
    public ResponseEntity<Void> deletePost(String email, Long idPost) {
        User user = userRepository.findByEmail(email);
        Post post = postRepository.getById(idPost);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN")))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        PostList postList = postListRepository.findByPost(post);
        if (postList != null) {
            postListRepository.delete(postList);
            postRepository.delete(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        PostMovie postMovie = postMovieRepository.findByPost(post);
        if (postMovie != null) {
            postMovieRepository.delete(postMovie);
            postRepository.delete(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        PostPerson postPerson = postPersonRepository.findByPost(post);
        if (postPerson != null) {
            postPersonRepository.delete(postPerson);
            postRepository.delete(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}