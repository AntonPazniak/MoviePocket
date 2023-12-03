package com.moviePocket.repository.list;

import com.moviePocket.entities.list.MovieInList;
import com.moviePocket.entities.list.MovieList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MovieInListRepository extends JpaRepository<MovieInList, Long> {

    MovieInList findByMovieListAndIdMovie(MovieList movieList, Long id);

    List<MovieInList> getAllByMovieList(MovieList movieList);

    void deleteAllByMovieList(MovieList movieList);

    boolean existsById(Long id);
}
