package com.moviePocket.repository.list;

import com.moviePocket.entities.list.ListItem;
import com.moviePocket.entities.list.ListMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ListItemRepository extends JpaRepository<ListItem, Long> {

    ListItem findByMovieListAndMovie_id(ListMovie movieList, Long id);

    List<ListItem> getAllByMovieList(ListMovie movieList);

    List<ListItem> findAllByMovie_Id(Long idMovie);

    void deleteAllByMovieList(ListMovie movieList);

    boolean existsById(Long id);
}
