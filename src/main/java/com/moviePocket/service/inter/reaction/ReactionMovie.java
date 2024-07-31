package com.moviePocket.service.inter.reaction;

import com.moviePocket.db.entities.movie.Movie;

import java.util.List;

public interface ReactionMovie {

    void setOrDelete(Long idMovie);

    Boolean getReaction(Long idMovie);

    List<Movie> getAllMyReactions();

    Integer getCountReactionByIdMovie(Long idMovie);

}
