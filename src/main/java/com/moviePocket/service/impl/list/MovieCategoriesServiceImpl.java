package com.moviePocket.service.impl.list;

import com.moviePocket.entities.list.MovieCategories;
import com.moviePocket.repository.list.MovieCategoriesRepository;
import com.moviePocket.service.movie.list.MovieCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieCategoriesServiceImpl implements MovieCategoriesService {
    @Autowired
    private MovieCategoriesRepository movieCategoriesRepository;

    public List<MovieCategories> getAllCategories() {
        return movieCategoriesRepository.getAllCategories();
    }

}
