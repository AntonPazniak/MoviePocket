/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.post;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.post.*;
import com.moviePocket.db.entities.review.Review;
import com.moviePocket.db.entities.review.ReviewPost;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.list.MovieListRepository;
import com.moviePocket.db.repository.post.*;
import com.moviePocket.db.repository.review.LikeReviewRepository;
import com.moviePocket.db.repository.review.ReviewPostRepository;
import com.moviePocket.db.repository.review.ReviewRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.post.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikePostRepository likePostRepository;
    private final MovieListRepository movieListRepository;

    private final PostListRepository postListRepository;
    private final PostMovieRepository postMovieRepository;
    private final PostPersonRepository postPersonRepository;

    private final MovieServiceImpl movieService;
    private final ReviewPostRepository reviewPostRepository;
    private final LikeReviewRepository likeReviewRepository;
    private final ReviewRepository reviewRepository;

    public ResponseEntity<ParsPost> createPostList(String email, String title, String content, Long idList) {
        ListMovie movieList = movieListRepository.getById(idList);
        if (movieList != null) {
            Post post = createPost(email, title, content);
            if (post == null)
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            else {
                PostList postList = new PostList(movieList, post);
                postListRepository.save(postList);
                return ResponseEntity.ok(parsOnePost(post));
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ParsPost> createPostMovie(String email, String title, String content, Long idMovie) {
        Post post = createPost(email, title, content);
        if (post == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Movie movie = movieService.setMovieIfNotExist(idMovie);
        if (movie == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            PostMovie postMovie = new PostMovie(movie, post);
            postMovieRepository.save(postMovie);
            return ResponseEntity.ok(parsOnePost(post));
        }
    }

    public ResponseEntity<ParsPost> createPostPerson(String email, String title, String content, Long idPerson) {
        Post post = createPost(email, title, content);
        if (post == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            PostPerson postPerson = new PostPerson(idPerson, post);
            postPersonRepository.save(postPerson);
            return ResponseEntity.ok(parsOnePost(post));
        }
    }

    private Post createPost(String email, String title, String content) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return null;
        else {
            Post post = new Post(user, title, content);
            postRepository.save(post);
            return post;
        }
    }

    public ResponseEntity<Void> updatePost(String email, Long idPost, String title, String content) {
        User user = userRepository.findByEmail(email);
        Post post = postRepository.getById(idPost);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (post.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            post.setTitle(title);
            post.setContent(content);
            postRepository.save(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> deletePost(String email, Long idPost) {
        User user = userRepository.findByEmail(email);
        Post post = postRepository.getById(idPost);
        List<Review> reviewsPost = reviewPostRepository.findReviewsByPost(post);


        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (post.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (reviewsPost != null) {
            for (Review review : reviewsPost) {
                ReviewPost reviewPostEntity = reviewPostRepository.findByReview(review);

                if (reviewPostEntity != null) {
                    reviewPostRepository.delete(reviewPostEntity);
                    likeReviewRepository.deleteAllByReview(review);
                    reviewRepository.delete(review);
                }
            }
        }


        likePostRepository.deleteAllByPost(post);

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

    public ResponseEntity<List<ParsPost>> getAllByIdMovie(Long idMovie) {
        List<PostMovie> list = postMovieRepository.findAllByMovie_Id(idMovie);
        if (list != null) {
            List<Post> posts = new ArrayList<>();
            for (PostMovie p : list) {
                posts.add(p.getPost());
            }
            return ResponseEntity.ok(parsPost(posts));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<ParsPost>> getAllByIdPerson(Long idPerson) {
        List<PostPerson> list = postPersonRepository.findAllByIdPerson(idPerson);
        if (list != null) {
            List<Post> posts = new ArrayList<>();
            for (PostPerson p : list) {
                posts.add(p.getPost());
            }
            return ResponseEntity.ok(parsPost(posts));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<ParsPost>> getAllByUser(String email) {
        User user = userRepository.findByEmail(email);
        List<Post> posts = postRepository.findAllByUser(user);
        if (posts != null) {
            return ResponseEntity.ok(parsPost(posts));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<ParsPost>> getAllByIdList(Long idList) {
        List<PostList> list = postListRepository.findAllByMovieList_Id(idList);
        if (list != null) {
            List<Post> posts = new ArrayList<>();
            for (PostList p : list) {
                posts.add(p.getPost());
            }
            return ResponseEntity.ok(parsPost(posts));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Override
    public ResponseEntity<List<ParsPost>> getAllByTitle(String title) {
        List<Post> posts = postRepository.findAllByTitle(title);
        return ResponseEntity.ok(parsPost(posts));
    }

    @Override
    public ResponseEntity<List<ParsPost>> getAllByPartialTitle(String title) {
        if (title.equals(""))
            return ResponseEntity.ok(null);
        List<Post> posts = postRepository.findAllByPartialTitle(title);
        if (posts == null )
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(parsPost(posts));
    }

    @Override
    public ResponseEntity<ParsPost> getPost(Long idPost) {
        if (postRepository.existsById(idPost)) {
            Post post = postRepository.getById(idPost);
            List<Post> posts = new ArrayList<>();
            posts.add(post);
            return ResponseEntity.ok(parsPost(posts).get(0));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<ParsPost>> getAllMyPosts(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            List<Post> posts = postRepository.findAllByUser(user);
            return ResponseEntity.ok(parsPost(posts));
        }
    }

    @Override
    public ResponseEntity<List<ParsPost>> getAllByUsernamePosts(String username) {
        User user = userRepository.findByUsernameAndAccountActive(username, true);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            List<Post> posts = postRepository.findAllByUser(user);
            return ResponseEntity.ok(parsPost(posts));
        }
    }

    @Override
    public ResponseEntity<List<ParsPost>> getNewestPosts() {

        List<Post> posts = postRepository.findAll();
        Collections.sort(posts, Comparator.comparing(Post::getCreated).reversed());
        return ResponseEntity.ok(parsPost(posts));
    }

    @Override
    public ResponseEntity<List<ParsPost>> getOldestPosts() {
        List<Post> posts = postRepository.findAll();
        Collections.sort(posts, Comparator.comparing(Post::getCreated));
        return ResponseEntity.ok(parsPost(posts));
    }

    public List<ParsPost> parsPost(List<Post> posts) {
        List<ParsPost> parsPostLL = new ArrayList<>();
        for (Post post : posts) {
            parsPostLL.add(parsOnePost(post));
        }
        return parsPostLL;
    }

    public ParsPost parsOnePost(Post post) {
        Long idAvatar = null;

        if (post.getUser().getAvatar() != null)
            idAvatar = post.getUser().getAvatar().getId();

        int[] likeAndDis = new int[]{likePostRepository.countByPostAndLickOrDisIsTrue(post),
                likePostRepository.countByPostAndLickOrDisIsFalse(post)};
        ParsPost parsPost = new ParsPost(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                likeAndDis,
                new UserPostDto(post.getUser().getUsername(), idAvatar),
                post.getCreated(),
                post.getUpdated()
        );
        PostList postList = postListRepository.findByPost(post);
        PostMovie postMovie = postMovieRepository.findByPost(post);
        PostPerson postPerson = postPersonRepository.findByPost(post);
        if (postList != null) {
            parsPost.setIdList(postList.getMovieList().getId());
        } else if (postMovie != null) {
            parsPost.setIdMovie(postMovie.getMovie().getId());
        } else if (postPerson != null) {
            parsPost.setIdPerson(postPerson.getIdPerson());
        }

        return parsPost;
    }

    public ResponseEntity<Boolean> authorshipCheck(Long idPost, String username) {
        try {
            User user = userRepository.findByEmail(username);
            Post post = postRepository.getById(idPost);
            return ResponseEntity.ok(post.getUser().equals(user));
        } catch (EntityNotFoundException e) {
            System.out.println(e);
        }
        return ResponseEntity.ok(false);
    }

    public ResponseEntity<List<ParsPost>> getTop10LatestPosts() {
        List<Post> posts = postRepository.findTop10LatestPosts();
        if (!posts.isEmpty())
            return ResponseEntity.ok(parsPost(posts));
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<ParsPost>> getTop10LikedPosts() {
        List<Post> posts = postRepository.findTop10LikedPosts();
        if (!posts.isEmpty())
            return ResponseEntity.ok(parsPost(posts));
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
