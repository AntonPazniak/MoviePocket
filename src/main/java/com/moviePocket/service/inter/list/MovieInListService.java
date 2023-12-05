package com.moviePocket.service.inter.list;

import org.springframework.http.ResponseEntity;

public interface MovieInListService {
    ResponseEntity<Void> addOrDelMovieFromList(String email, Long idList, Long idMovie);

}
