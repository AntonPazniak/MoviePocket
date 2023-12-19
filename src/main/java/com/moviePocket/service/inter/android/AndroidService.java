package com.moviePocket.service.inter.android;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface AndroidService {
    ResponseEntity<JSONObject> getData(String url);

}
