package com.moviePocket.controller.movie.tracking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies/tracking")
@RequiredArgsConstructor
public class MovieTrackingController {

    @PostMapping("/set")
    public ResponseEntity<Void> setOrDeleteMovieWatched(@RequestParam("idMovie") Long idMovie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();


        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


}
