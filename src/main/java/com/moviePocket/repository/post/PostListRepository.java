package com.moviePocket.repository.post;

import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.post.PostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PostListRepository extends JpaRepository<PostList, Long> {

    PostList findByPost(Post post);

    List<PostList> findAllByMovieList_Id(Long id);
}