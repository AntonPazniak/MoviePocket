package com.moviePocket.repository.list;

import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MovieListRepository extends JpaRepository<ListMovie, Long> {

    ListMovie getById(Long id);
    List<ListMovie> findAllByUser(User user);

    @Query("SELECT lm FROM ListMovie lm WHERE LOWER(lm.title) LIKE LOWER(CONCAT('%', :partialTitle, '%'))")
    List<ListMovie> findAllByPartialTitle(@Param("partialTitle") String partialTitle);


    @Query("SELECT lm FROM ListMovie lm JOIN lm.movies movie WHERE movie.id = :idMovie")
    List<ListMovie> findAllByidMovie(@Param("idMovie") Long idMovie);

    @Query("SELECT lm FROM ListMovie lm ORDER BY lm.created DESC")
    List<ListMovie> findTop10LatestLists();

    @Query("SELECT ll.movieList, COUNT(ll) as likeCount " +
            "FROM LikeList ll " +
            "WHERE ll.lickOrDis = true " +
            "GROUP BY ll.movieList " +
            "HAVING COUNT(ll) > 0 " +
            "ORDER BY likeCount DESC")
    List<ListMovie> findTop10LikedLists();


}
