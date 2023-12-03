package com.moviePocket.repository.list;

import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MovieListRepository extends JpaRepository<ListMovie, Long> {

    ListMovie getById(Long id);


    List<ListMovie> findAllByUser(User user);

    @Query("SELECT m FROM ListMovie m WHERE m.title LIKE :title%")
    List<ListMovie> findAllByTitle(String title);

}
