/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.tracking;

import com.moviePocket.service.inter.tracing.TracingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies/tracking")
@RequiredArgsConstructor
public class MovieTrackingController {

    @Autowired
    private TracingService tracingService;


    @PostMapping("/set")
    public ResponseEntity<Boolean> setOrDeleteMovieTracing(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return tracingService.setOrDelByIdMovie(email, idMovie);
    }

    @GetMapping("/get")
    public ResponseEntity<Boolean> existTracingByIdMovie(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return tracingService.existByIdMovie(email, idMovie);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCountTracingByIdMovie(@RequestParam("idMovie") Long idMovie) {
        return ResponseEntity.ok(tracingService.getCountByIdMovie(idMovie));
    }

    @GetMapping("/allMy")
    public ResponseEntity<Long[]> getAllMovieTracingByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return tracingService.getAllByUser(email);
    }


}
