package com.moviePocket.service.inter.post;

import com.moviePocket.entities.post.ParsPost;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {
    ResponseEntity<ParsPost> createPostList(String email, String title, String content, Long idList);

    ResponseEntity<ParsPost> createPostMovie(String email, String title, String content, Long idMovie);

    ResponseEntity<ParsPost> createPostPerson(String email, String title, String content, Long idPerson);

    ResponseEntity<Void> updatePost(String email, Long idPost, String title, String content);
    ResponseEntity<Void> deletePost(String email, Long idPost);

    ResponseEntity<List<ParsPost>> getAllByIdMovie(Long idMovie);

    ResponseEntity<List<ParsPost>> getAllByIdPerson(Long idPerson);

    ResponseEntity<List<ParsPost>> getAllByIdList(Long idList);
    ResponseEntity<List<ParsPost>> getAllByTitle(String title);

    ResponseEntity<ParsPost> getPost(Long idPost);

    ResponseEntity<List<ParsPost>> getAllMyPosts(String email);

    ResponseEntity<List<ParsPost>> getAllByUsernamePosts(String username);

    ResponseEntity<List<ParsPost>> getNewestPosts();

    ResponseEntity<List<ParsPost>> getOldestPosts();

    ResponseEntity<Boolean> authorshipCheck(Long idPost, String username);

    ResponseEntity<List<ParsPost>> getTop10LatestPosts();

    ResponseEntity<List<ParsPost>> getTop10LikedPosts();


}
