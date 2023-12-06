package com.moviePocket.service.inter.post;

import org.springframework.http.ResponseEntity;

public interface MovieListInPostService {
    ResponseEntity<Void> addOrDelMovieListFromPost(String email, Long idPost, Long idMovieList);
}
