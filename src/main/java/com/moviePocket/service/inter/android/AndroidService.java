package com.moviePocket.service.inter.android;

import org.springframework.http.ResponseEntity;

public interface AndroidService {
    ResponseEntity<String> get(String url);

}
