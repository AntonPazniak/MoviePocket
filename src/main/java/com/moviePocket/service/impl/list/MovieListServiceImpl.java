/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.list;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.controller.dto.list.ListDTO;
import com.moviePocket.controller.dto.review.ReactionDTO;
import com.moviePocket.controller.dto.review.ReviewDTO;
import com.moviePocket.db.entities.list.ListGenres;
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.list.ReactionList;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.list.MovieListRepository;
import com.moviePocket.exception.BadRequestException;
import com.moviePocket.exception.ForbiddenException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.list.MovieListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MovieListServiceImpl implements MovieListService {


    private final MovieListRepository listRepository;
    private final MovieServiceImpl movieService;

    private final AuthUser auth;

    @Override
    public ListDTO getList(Long idList) {
        return parsListToDTO(getListByIdOrThrow(idList));
    }

    @Override
    public List<ListDTO> getAllUserList() {
        User user = auth.getAuthenticatedUser();
        List<ListMovie> listMovieList = listRepository.findAllByUser(user);
        return listMovieList.stream().map(this::parsShortListToDTO).toList();
    }

    @Override
    public List<ListDTO> getAllByUsernameList(String username) {
        return listRepository.findByUser_username(username)
                .stream().map(this::parsShortListToDTO).toList();
    }

    @Override
    public ListDTO setList(String title, String content) {
        User user = auth.getAuthenticatedUser();
        var list = ListMovie.builder()
                .title(title)
                .content(content)
                .user(user)
                .reviews(List.of())
                .build();
        listRepository.save(list);
        return parsShortListToDTO(list);
    }

    @Override
    public ListDTO updateList(Long idList, String title, String content) {
        User user = auth.getAuthenticatedUser();
        var list = getListByIdOrThrow(idList);
        if (!list.getUser().equals(user)) {
            throw new ForbiddenException("Yor can't modify this list");
        } else {
            list.setTitle(title);
            list.setContent(content);
            listRepository.save(list);
            return parsShortListToDTO(list);
        }
    }

    @Override
    public void deleteList(Long idList) {
        User user = auth.getAuthenticatedUser();
        ListMovie list = getListByIdOrThrow(idList);
        if (!list.getUser().equals(user)) {
            throw new ForbiddenException("Yor can't delete this list");
        } else {
            listRepository.delete(list);
        }
    }

    @Override
    public List<ListDTO> getAllByPartialTitle(String title) {
        if (title.isEmpty())
            throw new BadRequestException("Title can't be empty");
        var movieLists = listRepository.findAllByPartialTitle(title);
        return movieLists.stream().map(this::parsShortListToDTO).toList();
    }

    @Override
    public boolean isMovieInList(Long idList, Long idMovie) {
        ListMovie list = getListByIdOrThrow(idList);
        return list.getMovies().stream().anyMatch(movie -> movie.getId().equals(idMovie));
    }

    @Override
    public List<ListDTO> getAllListsContainingMovie(Long idMovie) {
        List<ListMovie> listMovies = listRepository.findAllByidMovie(idMovie);
        return listMovies.stream().map(this::parsShortListToDTO).toList();
    }

    @Override
    public void addOrDelItemList(Long idList, Long idMovie) {
        User user = auth.getAuthenticatedUser();
        var list = getListByIdOrThrow(idList);
        if (!list.getUser().equals(user)) {
            throw new ForbiddenException("Yor can't modify this list");
        } else {
            Movie movie = movieService.setMovieIfNotExist(idMovie);
            if (list.getMovies().contains(movie)) {
                list.getMovies().remove(movie);
            } else {
                list.getMovies().add(movie);
            }
            listRepository.save(list);
        }
    }

    @Override
    public boolean authorshipCheck(Long idList) {
        User user = auth.getAuthenticatedUser();
        ListMovie list = getListByIdOrThrow(idList);
        return list.getUser().equals(user);
    }

    @Override
    public List<ListDTO> getTop10LatestLists() {
        List<ListMovie> list = listRepository.findTop10LatestLists();
        return list.stream().map(this::parsShortListToDTO).toList();
    }

    @Override
    public List<ListDTO> getTop10LikedLists() {
        List<ListMovie> listMovies = listRepository.findTop10LikedLists();
        return listMovies.stream().map(this::parsShortListToDTO).toList();
    }

    public ListMovie getListByIdOrThrow(Long id) {
        return listRepository.findById(id)
                .orElseThrow(() -> new com.moviePocket.exception.NotFoundException("List with id " + id + " not found"));
    }

    public ListDTO parsShortListToDTO(ListMovie list) {
        return ListDTO.builder()
                .id(list.getId())
                .title(list.getTitle())
                .content(list.getContent())
                .poster(list.getPoster() != null ? list.getPoster().getId() : null)
                .user(UserPostDto.builder()
                        .username(list.getUser().getLogin())
                        .avatar(list.getUser().getAvatar() != null ? list.getUser().getAvatar().getId() : null)
                        .build())
                .create(list.getCreated())
                .update(list.getUpdated())
                .build();
    }

    public ListDTO parsListToDTO(ListMovie list) {
        return ListDTO.builder()
                .id(list.getId())
                .title(list.getTitle())
                .content(list.getContent())
                .poster(list.getPoster() != null ? list.getPoster().getId() : null)
                .reaction(ReactionDTO.builder()
                        .likes((int) list.getReactions().stream().filter(ReactionList::isReaction).count())
                        .dislikes((int) list.getReactions().stream().filter(reaction -> !reaction.isReaction()).count())
                        .build())
                .user(UserPostDto.builder()
                        .username(list.getUser().getLogin())
                        .avatar(list.getUser().getAvatar() != null ? list.getUser().getAvatar().getId() : null)
                        .build())
                .movies(list.getMovies())
                .genres(list.getListGenres().stream().map(ListGenres::getGenre).toList())
                .create(list.getCreated())
                .update(list.getUpdated())
                .review(list.getReviews().stream().map(e -> ReviewDTO.parsReview(e.getReview())).toList())
                .build();
    }
}
