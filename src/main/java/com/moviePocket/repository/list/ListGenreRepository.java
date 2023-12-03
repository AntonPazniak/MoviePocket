package com.moviePocket.repository.list;

import com.moviePocket.entities.list.ListGenres;
import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.movie.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ListGenreRepository extends JpaRepository<ListGenres, Long> {

    ListGenres getByMovieListAndGenre(ListMovie movieList, Genre genre);

    void deleteAllByMovieList(ListMovie movieList);

    List<ListGenres> getAllByMovieList(ListMovie movieList);

}
