package com.moviePocket.service.impl.post;

import com.moviePocket.entities.post.MovieListInPost;
import com.moviePocket.entities.post.Post;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.post.MovieListInPostRepository;
import com.moviePocket.repository.post.PostRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.movie.post.MovieListInPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ListItemServiceImpl implements MovieListInPostService {

    @Autowired
    private MovieListInPostRepository movieListInPostRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Void> addOrDelMovieListFromPost(String email, Long idPost, Long idMovieList) {
        User user = userRepository.findByEmail(email);
        Post post = postRepository.getById(idPost);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (post.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            MovieListInPost movieListInPost = movieListInPostRepository.findByPostAndIdMovieList(post, idMovieList);
            if (movieListInPost == null) {
                movieListInPostRepository.save(new MovieListInPost(post, idMovieList));
            } else {
                movieListInPostRepository.delete(movieListInPost);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
