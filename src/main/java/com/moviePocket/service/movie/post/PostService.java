package com.moviePocket.service.movie.post;

import com.moviePocket.entities.movie.post.ParsPost;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {
    ResponseEntity<Void> setPost(String email, String title, String content);
    
    ResponseEntity<Void> updatePostTitle(String email, Long idPost, String title);

    ResponseEntity<Void> updatePostContent(String email, Long idPost, String content);

    ResponseEntity<Void> deletePost(String email, Long idPost);

    ResponseEntity<List<ParsPost>> getAllByTitle(String title);

    ResponseEntity<List<ParsPost>> getPost(Long idPost);

    ResponseEntity<List<ParsPost>> getAllPosts();

    ResponseEntity<List<ParsPost>> getAllMyPosts(String email);

    ResponseEntity<List<ParsPost>> getAllByUsernamePosts(String username);

    ResponseEntity<List<ParsPost>> getNewestPosts();

    ResponseEntity<List<ParsPost>> getOldestPosts();

}
