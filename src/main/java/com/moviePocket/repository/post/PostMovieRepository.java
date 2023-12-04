package com.moviePocket.repository.post;

import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.post.PostMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PostMovieRepository extends JpaRepository<PostMovie, Long> {

    PostMovie findByPost(Post post);

    List<PostMovie> findAllByMovie_Id(Long idMovie);

}