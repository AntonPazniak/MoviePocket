package com.moviePocket.service.impl.list;


import com.moviePocket.entities.list.ListGenres;
import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.movie.Genre;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.list.ListGenreRepository;
import com.moviePocket.repository.list.MovieListRepository;
import com.moviePocket.repository.movie.GenreRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.inter.list.CategoriesMovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;

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


}