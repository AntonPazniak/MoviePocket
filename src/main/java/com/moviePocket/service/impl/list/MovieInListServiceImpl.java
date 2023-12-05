package com.moviePocket.service.impl.list;

import com.moviePocket.entities.list.ListItem;
import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.list.ListItemRepository;
import com.moviePocket.repository.list.MovieListRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.list.MovieInListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MovieInListServiceImpl implements MovieInListService {
    @Autowired
    private ListItemRepository movieInListRepository;
    @Autowired
    private MovieListRepository movieListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieServiceImpl movieService;

    @Transactional
    public ResponseEntity<Void> addOrDelMovieFromList(String email, Long idList, Long idMovie) {
        User user = userRepository.findByEmail(email);
        ListMovie movieList = movieListRepository.getById(idList);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (movieList.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            ListItem movieInList = movieInListRepository.findByMovieListAndMovie_id(movieList, idMovie);
            if (movieInList == null) {
                Movie movie = movieService.setMovie(idMovie);
                if (movie != null)
                    movieInListRepository.save(new ListItem(movieList, movie));
                else
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                movieInListRepository.delete(movieInList);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
