package com.moviePocket.service.impl.list;

import com.moviePocket.entities.list.ListGenres;
import com.moviePocket.entities.list.ListItem;
import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.list.ParsList;
import com.moviePocket.entities.movie.Genre;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.list.LikeListRepository;
import com.moviePocket.repository.list.ListGenreRepository;
import com.moviePocket.repository.list.ListItemRepository;
import com.moviePocket.repository.list.MovieListRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.list.MovieListService;
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

    private final ListItemRepository listItemRepository;

    private final LikeListRepository likeListRepository;

    private final ListGenreRepository listGenreRepository;
    @Transactional
    public ResponseEntity<Void> setMovieList(String email, String title, String content) throws NotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        ListMovie movieList = new ListMovie(title, content, user);
        movieListRepository.save(movieList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Void> updateMovieListTitle(String email, Long idMovieList, String title) {
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
            movieListRepository.save(movieList);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<Void> updateMovieListContent(String email, Long idMovieList, String content) {
        User user = userRepository.findByEmail(email);
        ListMovie movieList = movieListRepository.getById(idMovieList);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (movieList.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            movieList.setContent(content);
            movieListRepository.save(movieList);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    @Transactional
    public ResponseEntity<Void> deleteMovieList(String email, Long idMovieList) {
        User user = userRepository.findByEmail(email);
        ListMovie movieList = movieListRepository.getById(idMovieList);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (movieList == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (movieList.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            listItemRepository.deleteAllByMovieList(movieList);
            likeListRepository.deleteAllByMovieList(movieList);
            listGenreRepository.deleteAllByMovieList(movieList);
            movieListRepository.delete(movieList);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<ParsList> getMovieList(Long idMovieList) {
        if (movieListRepository.existsById(idMovieList)) {
            ListMovie movieList = movieListRepository.getById(idMovieList);
            return ResponseEntity.ok(parsList(movieList));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<ParsList>> getAllList() {
        List<ListMovie> movieList = movieListRepository.findAll();
        return ResponseEntity.ok(parsLists(movieList));
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
        List<ListItem> listItems = listItemRepository.getAllByMovieList(listMovie);
        List<Movie> movies = new ArrayList<>();
        for (ListItem listItem : listItems) {
            movies.add(listItem.getMovie());
        }
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
                movies,
                likeAndDis,
                listMovie.getUser().getUsername(),
                listMovie.getCreated(),
                listMovie.getUpdated()
        );
        return parsMovieList;
    }

//    public ResponseEntity<List<ParsMovieList>> getAllListExistIdMovie(Long idMovie) {
//        List<MovieList> lists = movieListRepository.findMovieListByIdMovie(idMovie);
//        return ResponseEntity.ok(parsList(lists));
//    }


}
