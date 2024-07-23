/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

//package com.moviePocket.service.impl.admin;
//
//import com.moviePocket.controller.dto.UserPostDto;
//import com.moviePocket.db.entities.post.Post;
//import com.moviePocket.db.entities.post.PostList;
//import com.moviePocket.db.entities.post.PostMovie;
//import com.moviePocket.db.entities.post.PostPerson;
//import com.moviePocket.db.entities.review.Review;
//import com.moviePocket.db.entities.review.ReviewList;
//import com.moviePocket.db.entities.review.ReviewMovie;
//import com.moviePocket.db.entities.review.ReviewPost;
//import com.moviePocket.db.entities.user.BlockedUser;
//import com.moviePocket.db.entities.user.ParsBlockedUser;
//import com.moviePocket.db.entities.user.User;
//import com.moviePocket.db.repository.admin.BlockedUserRepository;
//import com.moviePocket.db.repository.post.PostListRepository;
//import com.moviePocket.db.repository.post.PostMovieRepository;
//import com.moviePocket.db.repository.post.PostPersonRepository;
//import com.moviePocket.db.repository.post.PostRepository;
//import com.moviePocket.db.repository.review.*;
//import com.moviePocket.db.repository.user.UserRepository;
//import com.moviePocket.service.inter.admin.BlockedUserService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BlockedUserServiceImpl implements BlockedUserService {
//
//    private final BlockedUserRepository blockedUserRepository;
//    private final UserRepository userRepository;
//    private final ReviewListRepository reviewListRepository;
//    private final ReviewPostRepository reviewPostRepository;
//    private final ReviewMovieRepository reviewMovieRepository;
//    private final ReviewRepository reviewRepository;
//    private final LikeReviewRepository likeReviewRepository;
//    private final PostRepository postRepository;
//    private final PostListRepository postListRepository;
//    private final PostMovieRepository postMovieRepository;
//    private final PostPersonRepository postPersonRepository;
//
//
//    public BlockedUser saveBlockedUser(BlockedUser blockedUser) {
//
//        return blockedUserRepository.save(blockedUser);
//    }
//
//    public ResponseEntity<List<ParsBlockedUser>> getAllBlockedUsers() {
//        List<BlockedUser> blockedUsers = blockedUserRepository.findAll();
//        return ResponseEntity.ok(parsBlockedUserList(blockedUsers));
//    }
//
//    private List<ParsBlockedUser> parsBlockedUserList(List<BlockedUser> blockedUsers) {
//        List<ParsBlockedUser> parsBlockedUsers = new ArrayList<>();
//        for (BlockedUser blockedUser : blockedUsers) {
//            parsBlockedUsers.add(parsBlockedUser(blockedUser));
//        }
//
//        return parsBlockedUsers;
//    }
//
//    private ParsBlockedUser parsBlockedUser(BlockedUser blockedUser) {
//        User user = userRepository.findByEmail(blockedUser.getUser().getEmail());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long idAvatar = null;
//        if (user.getAvatar() != null)
//            idAvatar = user.getAvatar().getId();
//
//        return new ParsBlockedUser(
//                new UserPostDto(user.getUsername(), idAvatar),
//                user.getUpdated(),
//                new UserPostDto(authentication.getName(), idAvatar),
//                blockedUser.getComment()
//        );
//    }
//
//    public BlockedUser findById(Long userId) {
//        return blockedUserRepository.findByUser_Id(userId);
//    }
//
//    @Override
//    public void delete(BlockedUser blockedUser) {
//        blockedUserRepository.delete(blockedUser);
//    }
//
//    @Transactional
//    public ResponseEntity<Void> delAdminReview(Long idReview, String username) {
//        User user = userRepository.findByEmail(username);
//        Review review = reviewRepository.getById(idReview);
//
//        if (user == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        } else if (review == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        if (user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        } else {
//            ReviewMovie reviewMovie = reviewMovieRepository.findByReview(review);
//            if (reviewMovie != null) {
//                reviewMovieRepository.delete(reviewMovie);
//                likeReviewRepository.deleteAllByReview(review);
//                reviewRepository.delete(review);
//                return new ResponseEntity<>(HttpStatus.OK);
//            }
//            ReviewList reviewList = reviewListRepository.findByReview(review);
//            if (reviewList != null) {
//                reviewListRepository.delete(reviewList);
//                likeReviewRepository.deleteAllByReview(review);
//                reviewRepository.delete(review);
//                return new ResponseEntity<>(HttpStatus.OK);
//            }
//            ReviewPost reviewPost = reviewPostRepository.findByReview(review);
//            if (reviewPost != null) {
//                reviewPostRepository.delete(reviewPost);
//                likeReviewRepository.deleteAllByReview(review);
//                reviewRepository.delete(review);
//            }
//        }
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<Void> deletePost(String email, Long idPost) {
//        User user = userRepository.findByEmail(email);
//        Post post = postRepository.getById(idPost);
//        if (user == null)
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        else if (post == null)
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        else if (user.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN")))
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        PostList postList = postListRepository.findByPost(post);
//        if (postList != null) {
//            postListRepository.delete(postList);
//            postRepository.delete(post);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        PostMovie postMovie = postMovieRepository.findByPost(post);
//        if (postMovie != null) {
//            postMovieRepository.delete(postMovie);
//            postRepository.delete(post);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        PostPerson postPerson = postPersonRepository.findByPost(post);
//        if (postPerson != null) {
//            postPersonRepository.delete(postPerson);
//            postRepository.delete(post);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//}
