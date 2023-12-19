package com.moviePocket.service.inter.android;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface AndroidService {
    ResponseEntity<JsonNode> getData(String url);

}
