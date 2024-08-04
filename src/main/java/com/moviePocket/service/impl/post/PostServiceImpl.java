/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.post;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.controller.dto.post.PostDTO;
import com.moviePocket.controller.dto.review.ReactionDTO;
import com.moviePocket.db.entities.Module;
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.post.*;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.post.PostListRepository;
import com.moviePocket.db.repository.post.PostMovieRepository;
import com.moviePocket.db.repository.post.PostPersonRepository;
import com.moviePocket.db.repository.post.PostRepository;
import com.moviePocket.exception.ForbiddenException;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.list.MovieListServiceImpl;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.post.PostService;
import com.moviePocket.util.ModelsConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AuthUser auth;
    private final MovieListServiceImpl listService;

    private final PostListRepository postListRepository;
    private final PostMovieRepository postMovieRepository;
    private final PostPersonRepository postPersonRepository;

    private final MovieServiceImpl movieService;

    private Post createPost(String title, String content, Module module, Long idItem) {
        User user = auth.getAuthenticatedUser();
        Post post = Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .module(module)
                .idItem(idItem)
                .build();
        postRepository.save(post);
        return post;
    }

    @Override
    public PostDTO createPostList(String title, String content, Long idList) {
        Post post = createPost(title, content, ModelsConstant.post, idList);
        ListMovie list = listService.getListByIdOrThrow(idList);
        var postList = PostList.builder()
                .post(post)
                .movieList(list)
                .build();
        postListRepository.save(postList);
        return parsPostToShortDTO(post);
    }

    @Override
    public PostDTO createPostMovie(String title, String content, Long idMovie) {
        Post post = createPost(title, content, ModelsConstant.movie, idMovie);
        Movie movie = movieService.setMovieIfNotExist(idMovie);
        PostMovie postMovie = PostMovie.builder()
                .movie(movie)
                .post(post)
                .build();
        postMovieRepository.save(postMovie);
        return parsPostToShortDTO(post);
    }

    @Override
    public PostDTO createPostPerson(String title, String content, Long idPerson) {
        Post post = createPost(title, content, ModelsConstant.person, idPerson);
        PostPerson postPerson = PostPerson.builder()
                .idPerson(idPerson)
                .post(post)
                .build();
        postPersonRepository.save(postPerson);
        return parsPostToShortDTO(post);
    }

    @Override
    public PostDTO updatePost(Long idPost, String title, String content) {
        User user = auth.getAuthenticatedUser();
        Post post = getPostByIdOrThrow(idPost);
        if (!post.getUser().equals(user)) {
            throw new ForbiddenException("You don't have permission to modify this post");
        } else {
            post.setTitle(title);
            post.setContent(content);
            postRepository.save(post);
        }
        return null;
    }

    @Override
    public void deletePost(Long idPost) {
        User user = auth.getAuthenticatedUser();
        Post post = getPostByIdOrThrow(idPost);

        if (!post.getUser().equals(user)) {
            throw new ForbiddenException("You don't have permission to delete this post");
        }

        var postList = postListRepository.findByPost(post);
        if (postList.isPresent()) {
            postListRepository.delete(postList.get());
            postRepository.delete(post);
        } else {
            var postMovie = postMovieRepository.findByPost(post);
            if (postMovie.isPresent()) {
                postMovieRepository.delete(postMovie.get());
                postRepository.delete(post);
            } else {
                var postPerson = postPersonRepository.findByPost(post);
                if (postPerson.isPresent()) {
                    postPersonRepository.delete(postPerson.get());
                    postRepository.delete(post);
                }
            }
        }
    }

    public List<PostDTO> getAllByIdMovie(Long idMovie) {
        return postMovieRepository.findAllByMovie_Id(idMovie)
                .stream().map(e -> parsPostToShortDTO(e.getPost())).toList();
    }

    public List<PostDTO> getAllByIdPerson(Long idPerson) {
        return postPersonRepository.findAllByIdPerson(idPerson)
                .stream().map(e -> parsPostToShortDTO(e.getPost())).toList();
    }

    @Override
    public List<PostDTO> getAllByUser() {
        User user = auth.getAuthenticatedUser();
        return postRepository.findAllByUser(user)
                .stream().map(this::parsPostToShortDTO).toList();
    }

    public List<PostDTO> getAllByIdList(Long idList) {
        return postListRepository.findAllByMovieList_Id(idList)
                .stream().map(e -> parsPostToShortDTO(e.getPost())).toList();
    }

    @Override
    public List<PostDTO> findAllByTitle(String title) {
        return postRepository.findAllByTitle(title)
                .stream().map(this::parsPostToShortDTO).toList();
    }


    @Override
    public List<PostDTO> getAllByPartialTitle(String title) {
        return postRepository.findAllByPartialTitle(title)
                .stream().map(this::parsPostToShortDTO).toList();
    }

    @Override
    public PostDTO getPost(Long idPost) {
        return parsPostToShortDTO(
                getPostByIdOrThrow(idPost)
        );

    }


    @Override
    public List<PostDTO> getAllByUsernamePosts(String username) {
        return postRepository.findAllByUser_Username(username)
                .stream().map(this::parsPostToShortDTO).toList();
    }

    @Override
    public List<PostDTO> getNewestPosts() {
        List<Post> posts = postRepository.findAll();
        posts.sort(Comparator.comparing(Post::getCreated).reversed());
        return posts.stream().map(this::parsPostToShortDTO).toList();
    }

    @Override
    public List<PostDTO> getOldestPosts() {
        List<Post> posts = postRepository.findAll();
        posts.sort(Comparator.comparing(Post::getCreated));
        return posts.stream().map(this::parsPostToShortDTO).toList();
    }

    public PostDTO parsPostToShortDTO(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .idModule(post.getModule().getId())
                .idItem(post.getIdItem())
                .title(post.getTitle())
                .content(post.getContent())
                .create(post.getCreated())
                .update(post.getUpdated())
                .reaction(ReactionDTO.builder()
                        .likes((int) post.getReactions().stream().filter(ReactionPost::isReaction).count())
                        .dislikes((int) post.getReactions().stream().filter(reactionPost -> !reactionPost.isReaction()).count())
                        .build())
                .user(UserPostDto.builder()
                        .avatar(post.getUser().getAvatar() != null ? post.getUser().getAvatar().getId() : null)
                        .username(post.getUser().getUsername())
                        .build()
                )
                .build();
    }

    @Override
    public boolean authorshipCheck(Long idPost) {
        User user = auth.getAuthenticatedUser();
        Post post = getPostByIdOrThrow(idPost);
        return post.getUser().equals(user);
    }

    public List<PostDTO> getTop10LatestPosts() {
        return postRepository.findTop10LatestPosts()
                .stream().map(this::parsPostToShortDTO).toList();
    }

    public List<PostDTO> getTop10LikedPosts() {
        return postRepository.findTop10LikedPosts()
                .stream().map(this::parsPostToShortDTO).toList();
    }

    public Post getPostByIdOrThrow(Long idPost) {
        return postRepository.findById(idPost).orElseThrow(
                () -> new NotFoundException("Post not found")
        );
    }

}
