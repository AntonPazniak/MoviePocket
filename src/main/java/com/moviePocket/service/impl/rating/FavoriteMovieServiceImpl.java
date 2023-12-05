package com.moviePocket.service.impl.rating;

import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.rating.FavoriteMovie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.rating.FavoriteMovieRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.rating.FavoriteMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteMovieServiceImpl implements FavoriteMovieService {

    private final FavoriteMovieRepository favoriteMoviesRepository;

    private final UserRepository userRepository;
    private final MovieServiceImpl movieService;

    @Transactional
    public ResponseEntity<Void> setOrDeleteNewFavoriteMovies(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        FavoriteMovie favoriteMovie = favoriteMoviesRepository.findByUserAndMovie_id(
                userRepository.findByEmail(email), idMovie);
        Movie movie = movieService.setMovie(idMovie);
        if (movie != null) {
            if (favoriteMovie == null) {
                favoriteMoviesRepository.save(new FavoriteMovie(userRepository.findByEmail(email), movie));
            } else { // if user already marked movie as fav, it will be deleted
                favoriteMoviesRepository.delete(favoriteMovie);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Boolean> getFromFavoriteMovies(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        FavoriteMovie favoriteMovie = favoriteMoviesRepository.findByUserAndMovie_id(
                userRepository.findByEmail(email), idMovie);
        return ResponseEntity.ok(favoriteMovie != null);
    }

    public ResponseEntity<List<Movie>> getAllUserFavoriteMovies(String email) {
        List<FavoriteMovie> favoriteMoviesList = favoriteMoviesRepository.findAllByUser(
                userRepository.findByEmail(email));
        List<Movie> movies = new ArrayList<>();
        for (FavoriteMovie favoriteMovies : favoriteMoviesList) {
            movies.add(favoriteMovies.getMovie());
        }
        if (movies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(movies);
    }

    public ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie) {
        int count = favoriteMoviesRepository.getAllCountByIdMovie(idMovie);
        return ResponseEntity.ok(count);
    }

}