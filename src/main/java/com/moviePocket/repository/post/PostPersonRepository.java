package com.moviePocket.repository.post;

import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.post.PostPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PostPersonRepository extends JpaRepository<PostPerson, Long> {

    PostPerson findByPost(Post post);

    List<PostPerson> findAllByIdPerson(Long idPerson);

}