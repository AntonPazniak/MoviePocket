package com.moviePocket.service.inter.list;

import org.springframework.http.ResponseEntity;

public interface LikeListService {
    ResponseEntity<Void> setLikeOrDisOrDel(String username, Long id, boolean likeOrDis);

    ResponseEntity<Boolean> getLikeOrDis(String username, Long id);

    ResponseEntity<Integer[]> getAllLikeAndDisByIdMovieList(Long idMovieList);
}
