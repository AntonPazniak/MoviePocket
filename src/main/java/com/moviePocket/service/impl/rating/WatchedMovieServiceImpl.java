package com.moviePocket.service.impl.rating;

import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.rating.WatchedMovie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.list.MovieListRepository;
import com.moviePocket.repository.rating.WatchedMovieRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.rating.WatchedMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WatchedMovieServiceImpl implements WatchedMovieService {

    private final WatchedMovieRepository watchedMovieRepository;
    private final UserRepository userRepository;
    private final MovieServiceImpl movieService;
    private final MovieListRepository movieListRepository;

    @Transactional
    public ResponseEntity<Void> setOrDeleteNewWatched(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        WatchedMovie watchedMovie = watchedMovieRepository.findByUserAndMovie_Id(
                user, idMovie);
        Movie movie = movieService.setMovie(idMovie);
        if (movie != null) {
            if (watchedMovie == null) {
                watchedMovieRepository.save(
                        new WatchedMovie(userRepository.findByEmail(email), movie));
            } else {
                watchedMovieRepository.delete(watchedMovie);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Boolean> getFromWatched(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        WatchedMovie watched = watchedMovieRepository.findByUserAndMovie_Id(
                userRepository.findByEmail(email), idMovie);
        return ResponseEntity.ok(watched != null);
    }

    public ResponseEntity<List<Movie>> getAllUserWatched(String email) {
        List<WatchedMovie> watchedList = watchedMovieRepository.findAllByUser(
                userRepository.findByEmail(email));
        List<Movie> movies = watchedList.stream()
                .map(WatchedMovie::getMovie)
                .collect(Collectors.toList());

        if (movies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(movies);
    }

    public ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie) {
        int count = watchedMovieRepository.getAllCountByIdMovie(idMovie);
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<Integer> getCountWatchedFromList(String email, Long idMovieList) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        ListMovie movieList = movieListRepository.getById(idMovieList);
        if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        int count = watchedMovieRepository.getCountWatchedFromList(user, idMovieList);
        return ResponseEntity.ok(count);
    }
}
