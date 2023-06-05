package com.moviePocket.repository.movie.list;

import com.moviePocket.entities.movie.list.MovieList;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MovieListRepository extends JpaRepository<MovieList, Long> {

    MovieList getById(Long id);

    List<MovieList> findAllByUser(User user);


}
