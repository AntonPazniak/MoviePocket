package com.moviePocket.service.list;

import com.moviePocket.entities.list.ParsList;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MovieListService {

    ResponseEntity<Void> setList(String email, String title, String content);

    ResponseEntity<Void> updateList(String email, Long idMovieList, String title, String content);

    ResponseEntity<Void> deleteList(String email, Long idMovieList);

    ResponseEntity<List<ParsList>> getAllByTitle(String title);

    ResponseEntity<ParsList> getList(Long idMovieList);

    ResponseEntity<List<ParsList>> getAllMyList(String email);

    ResponseEntity<List<ParsList>> getAllByUsernameList(String username);

    ResponseEntity<List<ParsList>> getAllListsContainingMovie(Long idMovie);

}