package com.moviePocket.service.list;

import org.springframework.http.ResponseEntity;

public interface LikeListService {
    ResponseEntity<Void> setLikeOrDisOrDel(String username, Long id, boolean likeOrDis);

    ResponseEntity<boolean[]> getLikeOrDis(String username, Long id);

    ResponseEntity<Integer[]> getAllLikeAndDisByIdMovieList(Long idMovieList);
}
