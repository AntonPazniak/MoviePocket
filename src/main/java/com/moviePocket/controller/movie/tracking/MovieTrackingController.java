package com.moviePocket.controller.movie.tracking;

import com.moviePocket.service.movie.tracing.TracingService;
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




}
