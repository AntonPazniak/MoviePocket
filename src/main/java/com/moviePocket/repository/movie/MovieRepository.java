package com.moviePocket.repository.movie;

import com.moviePocket.entities.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsById(Long idMovie);

}
