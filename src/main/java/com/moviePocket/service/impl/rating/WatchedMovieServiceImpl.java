package com.moviePocket.service.impl.rating;

import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.rating.WatchedMovie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.rating.WatchedMovieRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.rating.WatchedMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WatchedMovieServiceImpl implements WatchedMovieService {

    private final WatchedMovieRepository watchedMovieRepository;
    private final UserRepository userRepository;
    private final MovieServiceImpl movieService;

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
        List<Movie> movies = new ArrayList<>();
        for (WatchedMovie watched : watchedList) {
            movies.add(watched.getMovie());
        }
        if (movies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(movies);
    }

    public ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie) {
        int count = watchedMovieRepository.getAllCountByIdMovie(idMovie);
        return ResponseEntity.ok(count);
    }
}