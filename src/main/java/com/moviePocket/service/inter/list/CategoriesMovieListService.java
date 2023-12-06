package com.moviePocket.service.inter.list;

import org.springframework.http.ResponseEntity;

public interface CategoriesMovieListService {

    ResponseEntity<Void> setOrDelCategoryList(String email, Long idList, Long idCategory);

}
