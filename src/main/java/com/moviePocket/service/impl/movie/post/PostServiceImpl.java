package com.moviePocket.service.impl.movie.post;

import com.moviePocket.entities.post.MovieListInPost;
import com.moviePocket.entities.post.ParsPost;
import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.post.LikePostRepository;
import com.moviePocket.repository.post.MovieListInPostRepository;
import com.moviePocket.repository.post.PostRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.movie.post.PostService;
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
    private final MovieListInPostRepository movieListInPostRepository;
    private final LikePostRepository likePostRepository;


    @Override
    public ResponseEntity<Void> setPost(String email, String title, String content) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Post post = new Post(user, content, title);
        postRepository.save(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updatePostTitle(String email, Long idPost, String title) {
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
            postRepository.save(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> updatePostContent(String email, Long idPost, String content) {
        User user = userRepository.findByEmail(email);
        Post post = postRepository.getById(idPost);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (post.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            post.setContent(content);
            postRepository.save(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> deletePost(String email, Long idPost) {
        User user = userRepository.findByEmail(email);
        Post post = postRepository.getById(idPost);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (post.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            movieListInPostRepository.deleteAllByPost(post);
            likePostRepository.deleteAllByPost(post);
            postRepository.delete(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<ParsPost>> getAllByTitle(String title) {
        List<Post> posts = postRepository.findAllByTitle(title);
        return ResponseEntity.ok(parsPost(posts));
    }

    @Override
    public ResponseEntity<List<ParsPost>> getPost(Long idPost) {
        if (postRepository.existsById(idPost)) {
            Post post = postRepository.getById(idPost);
            List<Post> posts = new ArrayList<>();
            posts.add(post);
            return ResponseEntity.ok(parsPost(posts));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<ParsPost>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(parsPost(posts));
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
            List<MovieListInPost> movieListInPostList = movieListInPostRepository.getAllByPost(post);
            List<Long> idPost = new ArrayList<>();
            for (MovieListInPost movieListInPost : movieListInPostList) {
                idPost.add(movieListInPost.getIdMovieList());
            }
            int[] likeAndDis = new int[]{likePostRepository.countByPostAndLickOrDisIsTrue(post),
                    likePostRepository.countByPostAndLickOrDisIsFalse(post)};
            ParsPost parsPost = new ParsPost(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    idPost,
                    likeAndDis,
                    post.getUser().getUsername(),
                    post.getCreated(),
                    post.getUpdated()
            );
            parsPostLL.add(parsPost);
        }
        return parsPostLL;
    }
}
