/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.android;

import com.fasterxml.jackson.databind.JsonNode;
import com.moviePocket.service.inter.android.AndroidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/android")
public class AndroidController {

    private final AndroidService androidService;

    @Autowired
    public AndroidController(AndroidService androidService) {
        this.androidService = androidService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<JsonNode> existTracingByIdMovie(@RequestParam("url") String url) {
        return androidService.getData(url);
    }
}
