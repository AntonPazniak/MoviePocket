package com.moviePocket.service.movie.post;

import org.springframework.http.ResponseEntity;

public interface LikePostService {
    ResponseEntity<Void> setLikeOrDisOrDel(String username, Long id, boolean likeOrDis);

    ResponseEntity<boolean[]> getLikeOrDis(String username, Long id);

    ResponseEntity<Integer[]> getAllLikeAndDisByIdPost(Long idPost);
}
