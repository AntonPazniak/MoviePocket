package com.moviePocket.service.inter.list;

import com.moviePocket.entities.list.ParsList;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MovieListService {

    ResponseEntity<ParsList> setList(String email, String title, String content);

    ResponseEntity<Void> updateList(String email, Long idMovieList, String title, String content);

    ResponseEntity<Void> deleteList(String email, Long idMovieList);

    ResponseEntity<List<ParsList>> getAllByTitle(String title);

    ResponseEntity<ParsList> getList(Long idMovieList);

    ResponseEntity<List<ParsList>> getAllMyList(String email);

    ResponseEntity<List<ParsList>> getAllByUsernameList(String username);

    ResponseEntity<List<ParsList>> getAllListsContainingMovie(Long idMovie);

    ResponseEntity<Void> addOrDelItemLIst(String email, Long idList, Long idMovie);

    ResponseEntity<Boolean> authorshipCheck(Long idList, String username);

    ResponseEntity<Boolean> isMovieInList(Long idMovieList, Long idMovie);

}
