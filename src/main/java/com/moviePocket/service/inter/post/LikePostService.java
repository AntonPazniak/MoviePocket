package com.moviePocket.service.inter.post;

import com.moviePocket.entities.post.ParsPost;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LikePostService {
    ResponseEntity<Void> setLikeOrDisOrDel(String username, Long id, boolean likeOrDis);

    ResponseEntity<Boolean> getLikeOrDis(String username, Long id);

    ResponseEntity<Integer[]> getAllLikeAndDisByIdPost(Long idPost);

    ResponseEntity<List<ParsPost>> getMostLikedPosts();

    ResponseEntity<List<ParsPost>> getLeastLikedPosts();

}
