package com.moviePocket.repository.movie;

import com.moviePocket.entities.movie.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface GenreRepository extends JpaRepository<Genre, Long> {
    long count();
}