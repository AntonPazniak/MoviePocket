package com.moviePocket.repository.post;

import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post getById(Long id);

    List<Post> findAllByUser(User user);

    @Query("Select m from Post m where m.title like :title%")
    List<Post> findAllByTitle(String title);

    @Query("SELECT p FROM Post p ORDER BY p.created DESC")
    List<Post> findTop10LatestPosts();

    @Query("SELECT ll.post, COUNT(ll) as likeCount " +
            "FROM LikePost ll " +
            "WHERE ll.lickOrDis = true " +
            "GROUP BY ll.post " +
            "HAVING COUNT(ll) > 0 " +
            "ORDER BY likeCount DESC")
    List<Post> findTop10LikedPosts();

    @Query("SELECT lm FROM Post lm WHERE LOWER(lm.title) LIKE LOWER(CONCAT('%', :partialTitle, '%'))")
    List<Post> findAllByPartialTitle(@Param("partialTitle") String partialTitle);
}
