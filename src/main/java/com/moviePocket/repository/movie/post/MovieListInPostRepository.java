package com.moviePocket.repository.movie.post;

import com.moviePocket.entities.movie.post.MovieListInPost;
import com.moviePocket.entities.movie.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface MovieListInPostRepository extends JpaRepository<MovieListInPost, Long> {
    MovieListInPost findByPostAndIdMovieList(Post post, Long id);

    List<MovieListInPost> getAllByPost(Post post);

    void deleteAllByPost(Post post);

    boolean existsById(Long id);
}
