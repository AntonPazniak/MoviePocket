package com.moviePocket.repository.movie.list;

import com.moviePocket.entities.movie.list.MovieList;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MovieListRepository extends JpaRepository<MovieList, Long> {

    MovieList getById(Long id);


    List<MovieList> findAllByUser(User user);

    @Query("SELECT m FROM MovieList m WHERE m.title LIKE :title%")
    List<MovieList> findAllByTitle(String title);

    @Query("SELECT mil.movieList FROM MovieInList mil WHERE mil.idMovie = :idMovie")
    List<MovieList> findMovieListByIdMovie(@Param("idMovie") Long idMovie);




}
