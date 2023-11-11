package com.moviePocket.repository.movie.post;

import com.moviePocket.entities.movie.post.LikePost;
import com.moviePocket.entities.movie.post.Post;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    LikePost getByUserAndPost(User user, Post post);

    @Query("SELECT COUNT(lmr) FROM LikePost lmr WHERE lmr.post = :post AND lmr.lickOrDis = true")
    int countByPostAndLickOrDisIsTrue(@Param("movieList") Post post);

    @Query("SELECT COUNT(lmr) FROM LikePost lmr WHERE lmr.post = :post AND lmr.lickOrDis = false")
    int countByPostAndLickOrDisIsFalse(@Param("movieList") Post post);

    void deleteAllByMovieList(Post post);
}
