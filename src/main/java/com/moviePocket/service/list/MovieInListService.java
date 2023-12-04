package com.moviePocket.service.list;

import org.springframework.http.ResponseEntity;

public interface MovieInListService {
    ResponseEntity<Void> addOrDelMovieFromList(String email, Long idList, Long idMovie);

}
