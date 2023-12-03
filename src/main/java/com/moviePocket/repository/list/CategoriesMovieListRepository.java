package com.moviePocket.repository.list;

import com.moviePocket.entities.list.CategoriesMovieList;
import com.moviePocket.entities.list.MovieCategories;
import com.moviePocket.entities.list.MovieList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface CategoriesMovieListRepository extends JpaRepository<CategoriesMovieList, Long> {

    CategoriesMovieList getByMovieListAndMovieCategories(MovieList movieList, MovieCategories movieCategories);

    void deleteAllByMovieList(MovieList movieList);

    List<CategoriesMovieList> getAllByMovieList(MovieList movieList);

}
