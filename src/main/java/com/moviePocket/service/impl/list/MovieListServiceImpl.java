package com.moviePocket.service.impl.list;

import com.moviePocket.entities.list.ListGenres;
import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.list.ParsList;
import com.moviePocket.entities.movie.Genre;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.list.LikeListRepository;
import com.moviePocket.repository.list.ListGenreRepository;
import com.moviePocket.repository.list.MovieListRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.list.MovieListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieListServiceImpl implements MovieListService {


    private final MovieListRepository movieListRepository;

    private final UserRepository userRepository;

    private final LikeListRepository likeListRepository;

    private final ListGenreRepository listGenreRepository;

    private final MovieServiceImpl movieService;

    @Transactional
    public ResponseEntity<Void> setList(String email, String title, String content) throws NotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        ListMovie movieList = new ListMovie(title, content, user);
        movieListRepository.save(movieList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Void> updateList(String email, Long idMovieList, String title, String content) {
        User user = userRepository.findByEmail(email);
        ListMovie movieList = movieListRepository.getById(idMovieList);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (movieList.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            movieList.setTitle(title);
            movieList.setContent(content);
            movieListRepository.save(movieList);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<Void> deleteList(String email, Long idMovieList) {
        User user = userRepository.findByEmail(email);
        ListMovie movieList = movieListRepository.getById(idMovieList);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (movieList.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            likeListRepository.deleteAllByMovieList(movieList);
            listGenreRepository.deleteAllByMovieList(movieList);
            movieListRepository.delete(movieList);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<Void> addOrDelItemLIst(String email, Long idList, Long idMovie) {
        User user = userRepository.findByEmail(email);
        ListMovie movieList = movieListRepository.getById(idList);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (movieList.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            Movie movie = movieService.setMovie(idMovie);
            if (movie != null) {
                if (movieList.getMovies().contains(movie)) {
                    movieList.getMovies().remove(movie);
                } else {
                    movieList.getMovies().add(movie);
                }
                movieListRepository.save(movieList);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<ParsList> getList(Long idMovieList) {
        if (movieListRepository.existsById(idMovieList)) {
            ListMovie movieList = movieListRepository.getById(idMovieList);
            return ResponseEntity.ok(parsList(movieList));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<ParsList>> getAllMyList(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            List<ListMovie> movieList = movieListRepository.findAllByUser(user);
            return ResponseEntity.ok(parsLists(movieList));
        }
    }

    public ResponseEntity<List<ParsList>> getAllByUsernameList(String username) {
        User user = userRepository.findByUsernameAndAccountActive(username, true);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            List<ListMovie> movieList = movieListRepository.findAllByUser(user);
            return ResponseEntity.ok(parsLists(movieList));
        }
    }

    public ResponseEntity<List<ParsList>> getAllByTitle(String title) {
        List<ListMovie> movieLists = movieListRepository.findAllByTitle(title);
        return ResponseEntity.ok(parsLists(movieLists));
    }

    private List<ParsList> parsLists(List<ListMovie> movieList) {
        List<ParsList> parsMovieLL = new ArrayList<>();
        for (ListMovie listMovie : movieList) {
            parsMovieLL.add(parsList(listMovie));
        }
        return parsMovieLL;
    }

    private ParsList parsList(ListMovie listMovie) {
        List<ListGenres> ListGenres = listGenreRepository.getAllByMovieList(listMovie);
        List<Genre> genres = new ArrayList<>();
        for (ListGenres g : ListGenres) {
            genres.add(g.getGenre());
        }

        int[] likeAndDis = new int[]{likeListRepository.countByMovieReviewAndLickOrDisIsTrue(listMovie),
                likeListRepository.countByMovieReviewAndLickOrDisIsFalse(listMovie)};
        ParsList parsMovieList = new ParsList(
                listMovie.getId(),
                listMovie.getTitle(),
                listMovie.getContent(),
                genres,
                listMovie.getMovies(),
                likeAndDis,
                listMovie.getUser().getUsername(),
                listMovie.getCreated(),
                listMovie.getUpdated()
        );
        return parsMovieList;
    }

    public ResponseEntity<List<ParsList>> getAllListsContainingMovie(Long idMovie) {
        List<ParsList> parsLists = new ArrayList<>();
        List<ListMovie> listMovies = movieListRepository.findAllByidMovie(idMovie);
        for (ListMovie list : listMovies) {
            int[] likeAndDis = new int[]{likeListRepository.countByMovieReviewAndLickOrDisIsTrue(list),
                    likeListRepository.countByMovieReviewAndLickOrDisIsFalse(list)};
            List<Genre> genres = new ArrayList<>();
            List<ListGenres> ListGenres = listGenreRepository.getAllByMovieList(list);
            for (ListGenres g : ListGenres) {
                genres.add(g.getGenre());
            }
            ParsList parsMovieList = new ParsList(
                    list.getId(),
                    list.getTitle(),
                    list.getContent(),
                    genres,
                    null,
                    likeAndDis,
                    list.getUser().getUsername(),
                    list.getCreated(),
                    list.getUpdated()
            );
            parsLists.add(parsMovieList);
        }
        return ResponseEntity.ok(parsLists);
    }


}
