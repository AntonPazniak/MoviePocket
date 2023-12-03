package com.moviePocket.service.impl.post;

import com.moviePocket.entities.post.LikePost;
import com.moviePocket.entities.post.ParsPost;
import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.post.LikePostRepository;
import com.moviePocket.repository.post.PostRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.movie.post.LikePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikePostServiceImpl implements LikePostService {

    private final LikePostRepository likePostRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostServiceImpl postService;

    @Override
    public ResponseEntity<Void> setLikeOrDisOrDel(String username, Long id, boolean likeOrDis) {
        Post post = postRepository.getById(id);
        User user = userRepository.findByEmail(username);
        LikePost likePost = likePostRepository.getByUserAndPost(user, post);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            if (likePost == null) {
                likePostRepository.save(new LikePost(post, user, likeOrDis));
            } else if (likePost.isLickOrDis() == likeOrDis) {
                likePostRepository.delete(likePost);
            } else {
                likePost.setLickOrDis(likeOrDis);
                likePostRepository.save(likePost);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<boolean[]> getLikeOrDis(String username, Long id) {
        Post post = postRepository.getById(id);
        User user = userRepository.findByEmail(username);
        LikePost likePost = likePostRepository.getByUserAndPost(user, post);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (likePost != null) {
            return ResponseEntity.ok(new boolean[]{likePost.isLickOrDis()});
        } else {
            return ResponseEntity.ok(new boolean[]{});
        }
    }

    @Override
    public ResponseEntity<Integer[]> getAllLikeAndDisByIdPost(Long idPost) {
        Post post = postRepository.getById(idPost);
        if (post != null) {
            return ResponseEntity.ok(new Integer[]{
                    likePostRepository.countByPostAndLickOrDisIsTrue(post),
                    likePostRepository.countByPostAndLickOrDisIsFalse(post)
            });
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<ParsPost>> getMostLikedPosts() {
        List<Post> posts = postRepository.findAll();

        // Sort the posts based on the total number of likes and dislikes
        posts.sort(Comparator.comparingInt(post -> {
            int likes = likePostRepository.countByPostAndLickOrDisIsTrue(post);
            int dislikes = likePostRepository.countByPostAndLickOrDisIsFalse(post);
            return likes - dislikes; // Sorting from most liked to least
        }));

        // Reverse the list to get it from most liked to least liked
        Collections.reverse(posts);

        return ResponseEntity.ok(postService.parsPost(posts));
    }

    @Override
    public ResponseEntity<List<ParsPost>> getLeastLikedPosts() {
        List<Post> posts = postRepository.findAll();

        // Sort the posts based on the total number of likes and dislikes
        posts.sort(Comparator.comparingInt(post -> {
            int likes = likePostRepository.countByPostAndLickOrDisIsTrue(post);
            int dislikes = likePostRepository.countByPostAndLickOrDisIsFalse(post);
            return likes - dislikes; // Sorting from most liked to least
        }));

        return ResponseEntity.ok(postService.parsPost(posts));
    }
}
