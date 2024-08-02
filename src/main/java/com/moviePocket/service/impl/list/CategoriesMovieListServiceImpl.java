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
import com.moviePocket.db.repository.movie.GenreRepository;
import com.moviePocket.exception.ForbiddenException;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.inter.list.CategoriesMovieListService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriesMovieListServiceImpl implements CategoriesMovieListService {

    private final ListGenreRepository categoriesMovieListRepository;
    private final MovieListServiceImpl movieListService;
    private final AuthUser auth;
    private final GenreRepository genreRepository;


    @Override
    public void setOrDelCategoryList(Long idList, Long idCategory) {
        User user = auth.getAuthenticatedUser();
        ListMovie movieList = movieListService.getListByIdOrThrow(idList);
        if (!movieList.getUser().equals(user)) {
            throw new ForbiddenException("You do not have permission to modify this category");
        } else {
            Genre genre = genreRepository.findById(idCategory)
                    .orElseThrow(() -> new NotFoundException("Genre not found"));
            var categoriesMovieList = categoriesMovieListRepository.
                    getByMovieListAndGenre(movieList, genre);
            if (categoriesMovieList.isPresent()) {
                categoriesMovieListRepository.delete(categoriesMovieList.get());
            } else {
                categoriesMovieListRepository.save(new ListGenres(movieList, genre));
            }
        }
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

}