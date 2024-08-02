package com.moviePocket.service.inter.reaction;

import java.util.List;

public interface ReactionService {

    void setReaction(boolean reaction);

    boolean getReaction(long id);

    <T> List<T> getAllMyReactions(); // Review or Movie or List

    Integer getCountReactionByIdM(Long idMovie);

}
