package com.moviePocket.service.movie.list;

import com.moviePocket.entities.list.ParsList;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MovieListService {

    ResponseEntity<Void> setMovieList(String email, String title, String content);

    ResponseEntity<Void> updateMovieListTitle(String email, Long idMovieList, String title);

    ResponseEntity<Void> updateMovieListContent(String email, Long idMovieList, String content);

    ResponseEntity<Void> deleteMovieList(String email, Long idMovieList);

    ResponseEntity<List<ParsList>> getAllByTitle(String title);

    ResponseEntity<ParsList> getMovieList(Long idMovieList);

    ResponseEntity<List<ParsList>> getAllList();

    ResponseEntity<List<ParsList>> getAllMyList(String email);

    ResponseEntity<List<ParsList>> getAllByUsernameList(String username);

    //ResponseEntity<List<ParsMovieList>> getAllListExistIdMovie(Long idMovie);

}
