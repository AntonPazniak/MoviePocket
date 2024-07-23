/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.list;


import com.moviePocket.db.entities.list.ListGenres;
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.movie.Genre;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.list.ListGenreRepository;
import com.moviePocket.db.repository.list.MovieListRepository;
import com.moviePocket.db.repository.movie.GenreRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.service.inter.list.CategoriesMovieListService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public class CategoriesMovieListServiceImpl implements CategoriesMovieListService {
    @Autowired
    private ListGenreRepository categoriesMovieListRepository;
    @Autowired
    private MovieListRepository movieListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Transactional
    public ResponseEntity<Void> setOrDelCategoryList(String email, Long idList, Long idGenre) throws NotFoundException {
        User user = userRepository.findByEmail(email);
        ListMovie movieList = movieListRepository.getById(idList);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!movieList.getUser().equals(user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            Genre genre = genreRepository.getById(idGenre);
            ListGenres categoriesMovieList = categoriesMovieListRepository.
                    getByMovieListAndGenre(movieList, genre);
            if (categoriesMovieList != null) {
                categoriesMovieListRepository.delete(categoriesMovieList);
            } else {
                categoriesMovieListRepository.save(new ListGenres(movieList, genre));
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<List<Genre>> getAll() {
        return ResponseEntity.ok(genreRepository.findAll());
    }

}