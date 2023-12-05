package com.moviePocket.service.inter.tracing;

import org.springframework.http.ResponseEntity;

public interface TracingService {


    ResponseEntity<Boolean> existByIdMovie(String email, Long idMovie);

    ResponseEntity<Boolean> setOrDelByIdMovie(String email, Long idMovie);

    ResponseEntity<Long[]> getAllByUser(String email);

    Long getCountByIdMovie(Long idMovie);


}
