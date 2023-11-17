package com.moviePocket.service.movie.tracing;

import org.springframework.http.ResponseEntity;

public interface TracingService {


    ResponseEntity<Boolean> existByIdMovie(String email, Long idMovie);

    ResponseEntity<Boolean> setOrDelByIdMovie(String email, Long idMovie);

    Long getCountByIdMovie(Long idMovie);


}
