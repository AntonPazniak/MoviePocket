package com.moviePocket.service.impl.post;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.entities.list.ListMovie;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.post.*;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.list.MovieListRepository;
import com.moviePocket.repository.post.*;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikePostRepository likePostRepository;
    private final MovieListRepository movieListRepository;

    private final PostListRepository postListRepository;
    private final PostMovieRepository postMovieRepository;
    private final PostPersonRepository postPersonRepository;

    private final MovieServiceImpl movieService;

    public ResponseEntity<Void> creatPostList(String email, String title, String content, Long idList) {
        ListMovie movieList = movieListRepository.getById(idList);
        if (movieList != null) {
            Post post = createPost(email, title, content);
            if (post == null)
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            else {
                PostList postList = new PostList(movieList, post);
                postListRepository.save(postList);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Void> creatPostMovie(String email, String title, String content, Long idMovie) {
        Post post = createPost(email, title, content);
        if (post == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Movie movie = movieService.setMovie(idMovie);
        if (movie == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            PostMovie postMovie = new PostMovie(movie, post);
            postMovieRepository.save(postMovie);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<Void> creatPostPerson(String email, String title, String content, Long idPerson) {
        Post post = createPost(email, title, content);
        if (post == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            PostPerson postPerson = new PostPerson(idPerson, post);
            postPersonRepository.save(postPerson);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private Post createPost(String email, String title, String content) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return null;
        else {
            Post post = new Post(user, title, content);
            postRepository.save(post);
            return post;
        }
    }

    public ResponseEntity<Void> updatePost(String email, Long idPost, String title, String content) {
        User user = userRepository.findByEmail(email);
        Post post = postRepository.getById(idPost);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (post.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            post.setTitle(title);
            post.setContent(content);
            postRepository.save(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> deletePost(String email, Long idPost) {
        User user = userRepository.findByEmail(email);
        Post post = postRepository.getById(idPost);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (post == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (post.getUser() != user) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        PostList postList = postListRepository.findByPost(post);
        if (postList != null) {
            postListRepository.delete(postList);
            postRepository.delete(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        PostMovie postMovie = postMovieRepository.findByPost(post);
        if (postMovie != null) {
            postMovieRepository.delete(postMovie);
            postRepository.delete(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        PostPerson postPerson = postPersonRepository.findByPost(post);
        if (postPerson != null) {
            postPersonRepository.delete(postPerson);
            postRepository.delete(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<ParsPost>> getAllByIdMovie(Long idMovie) {
        List<PostMovie> list = postMovieRepository.findAllByMovie_Id(idMovie);
        if (list != null) {
            List<Post> posts = new ArrayList<>();
            for (PostMovie p : list) {
                posts.add(p.getPost());
            }
            return ResponseEntity.ok(parsPost(posts));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<ParsPost>> getAllByIdPerson(Long idPerson) {
        List<PostPerson> list = postPersonRepository.findAllByIdPerson(idPerson);
        if (list != null) {
            List<Post> posts = new ArrayList<>();
            for (PostPerson p : list) {
                posts.add(p.getPost());
            }
            return ResponseEntity.ok(parsPost(posts));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<ParsPost>> getAllByIdList(Long idList) {
        List<PostList> list = postListRepository.findAllByMovieList_Id(idList);
        if (list != null) {
            List<Post> posts = new ArrayList<>();
            for (PostList p : list) {
                posts.add(p.getPost());
            }
            return ResponseEntity.ok(parsPost(posts));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Override
    public ResponseEntity<List<ParsPost>> getAllByTitle(String title) {
        List<Post> posts = postRepository.findAllByTitle(title);
        return ResponseEntity.ok(parsPost(posts));
    }

    @Override
    public ResponseEntity<List<ParsPost>> getPost(Long idPost) {
        if (postRepository.existsById(idPost)) {
            Post post = postRepository.getById(idPost);
            List<Post> posts = new ArrayList<>();
            posts.add(post);
            return ResponseEntity.ok(parsPost(posts));
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<ParsPost>> getAllMyPosts(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            List<Post> posts = postRepository.findAllByUser(user);
            return ResponseEntity.ok(parsPost(posts));
        }
    }

    @Override
    public ResponseEntity<List<ParsPost>> getAllByUsernamePosts(String username) {
        User user = userRepository.findByUsernameAndAccountActive(username, true);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            List<Post> posts = postRepository.findAllByUser(user);
            return ResponseEntity.ok(parsPost(posts));
        }
    }

    @Override
    public ResponseEntity<List<ParsPost>> getNewestPosts() {

        List<Post> posts = postRepository.findAll();
        Collections.sort(posts, Comparator.comparing(Post::getCreated).reversed());
        return ResponseEntity.ok(parsPost(posts));
    }

    @Override
    public ResponseEntity<List<ParsPost>> getOldestPosts() {
        List<Post> posts = postRepository.findAll();
        Collections.sort(posts, Comparator.comparing(Post::getCreated));
        return ResponseEntity.ok(parsPost(posts));
    }

    public List<ParsPost> parsPost(List<Post> posts) {
        List<ParsPost> parsPostLL = new ArrayList<>();
        for (Post post : posts) {
            int[] likeAndDis = new int[]{likePostRepository.countByPostAndLickOrDisIsTrue(post),
                    likePostRepository.countByPostAndLickOrDisIsFalse(post)};
            ParsPost parsPost = new ParsPost(
                    post.getId(),
                    post.getTitle(),
                    post.getContent(),
                    likeAndDis,
                    new UserPostDto(post.getUser().getUsername(), post.getUser().getAvatar().getId()),
                    post.getCreated(),
                    post.getUpdated()
            );
            PostList postList = postListRepository.findByPost(post);
            PostMovie postMovie = postMovieRepository.findByPost(post);
            PostPerson postPerson = postPersonRepository.findByPost(post);
            if (postList != null) {
                parsPost.setIdList(postList.getMovieList().getId());
            } else if (postMovie != null) {
                parsPost.setIdMovie(postMovie.getMovie().getId());
            } else if (postPerson != null) {
                parsPost.setIdPerson(postPerson.getIdPerson());
            }

            parsPostLL.add(parsPost);
        }
        return parsPostLL;
    }
}
