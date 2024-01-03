package com.moviePocket.service.impl.list;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.entities.image.ImageEntity;
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
import com.moviePocket.service.impl.image.ImageServiceImpl;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.list.MovieListService;
import com.moviePocket.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.persistence.EntityNotFoundException;
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
    private final ImageServiceImpl imageService;

    @Transactional
    public ResponseEntity<ListMovie> setList(String email, String title, String content) throws NotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        ListMovie movieList = new ListMovie(title, content, user);
        movieListRepository.save(movieList);
        return ResponseEntity.ok(movieList);
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
                int size = movieList.getMovies().size();
                ImageEntity image = movieList.getImageEntity();
                List<String> strings = new ArrayList<>();
                if (size >= 6) {
                    for (int i = 0; i < 6; i++) {
                        if (movieList.getMovies().get(i).getPoster_path() != null)
                            strings.add(Utils.TMDB_POSTER_PATH + movieList.getMovies().get(i).getPoster_path());
                        else
                            strings.add(Utils.BASS_POSTER_PATH);
                    }
                    movieList.setImageEntity(imageService.createMoviePoster(strings, movieList.getTitle() + ".jpg"));
                } else if (size >= 4) {
                    for (int i = 0; i < 4; i++) {
                        if (movieList.getMovies().get(i).getPoster_path() != null)
                            strings.add(Utils.TMDB_POSTER_PATH + movieList.getMovies().get(i).getPoster_path());
                        else
                            strings.add(Utils.BASS_POSTER_PATH);
                    }
                    movieList.setImageEntity(imageService.createMoviePoster(strings, movieList.getTitle() + ".jpg"));
                } else if (size >= 2) {
                    for (int i = 0; i < 2; i++) {
                        if (movieList.getMovies().get(i).getPoster_path() != null)
                            strings.add(Utils.TMDB_POSTER_PATH + movieList.getMovies().get(i).getPoster_path());
                        else
                            strings.add(Utils.BASS_POSTER_PATH);
                    }

                    movieList.setImageEntity(imageService.createMoviePoster(strings, movieList.getTitle() + ".jpg"));
                }
                movieListRepository.save(movieList);
                if (image != null)
                    imageService.delImage(image);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ParsList> getList(Long idMovieList) {
        if (movieListRepository.existsById(idMovieList)) {
            ListMovie movieList = movieListRepository.getById(idMovieList);
            return ResponseEntity.ok(parsListWithMovies(movieList));
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
            parsMovieLL.add(parsListWithoutMovies(listMovie));
        }
        return parsMovieLL;
    }

    private ParsList parsListWithMovies(ListMovie listMovie) {
        List<ListGenres> ListGenres = listGenreRepository.getAllByMovieList(listMovie);
        List<Genre> genres = new ArrayList<>();
        for (ListGenres g : ListGenres) {
            genres.add(g.getGenre());
        }

        int[] likeAndDis = new int[]{likeListRepository.countByMovieReviewAndLickOrDisIsTrue(listMovie),
                likeListRepository.countByMovieReviewAndLickOrDisIsFalse(listMovie)};

        Long poster = null;
        if (listMovie.getImageEntity() != null)
            poster = listMovie.getImageEntity().getId();
        Long idAvatar = null;
        if (listMovie.getUser().getAvatar() != null)
            idAvatar = listMovie.getUser().getAvatar().getId();
        return new ParsList(
                listMovie.getId(),
                listMovie.getTitle(),
                listMovie.getContent(),
                poster,
                genres,
                listMovie.getMovies(),
                likeAndDis,
                new UserPostDto(listMovie.getUser().getUsername(), idAvatar),
                listMovie.getCreated(),
                listMovie.getUpdated()
        );
    }

    private ParsList parsListWithoutMovies(ListMovie list) {
        int[] likeAndDis = new int[]{likeListRepository.countByMovieReviewAndLickOrDisIsTrue(list),
                likeListRepository.countByMovieReviewAndLickOrDisIsFalse(list)};
        List<Genre> genres = new ArrayList<>();
        List<ListGenres> ListGenres = listGenreRepository.getAllByMovieList(list);
        for (ListGenres g : ListGenres) {
            genres.add(g.getGenre());
        }
        Long poster = null;
        if (list.getImageEntity() != null)
            poster = list.getImageEntity().getId();
        Long idAvatar = null;
        if (list.getUser().getAvatar() != null)
            idAvatar = list.getUser().getAvatar().getId();
        return new ParsList(
                list.getId(),
                list.getTitle(),
                list.getContent(),
                poster,
                genres,
                null,
                likeAndDis,
                new UserPostDto(list.getUser().getUsername(), idAvatar),
                list.getCreated(),
                list.getUpdated()
        );
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
            parsLists.add(parsListWithoutMovies(list));
        }
        return ResponseEntity.ok(parsLists);
    }

    public ResponseEntity<Boolean> authorshipCheck(Long idList, String username) {
        try {
            User user = userRepository.findByEmail(username);
            ListMovie list = movieListRepository.getById(idList);
            return ResponseEntity.ok(list.getUser().equals(user));
        } catch (EntityNotFoundException e) {
            System.out.println(e);
        }
        return ResponseEntity.ok(false);
    }

}
