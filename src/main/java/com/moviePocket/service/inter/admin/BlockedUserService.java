/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.admin;

import com.moviePocket.db.entities.user.BlockedUser;
import com.moviePocket.db.entities.user.ParsBlockedUser;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BlockedUserService {
    BlockedUser saveBlockedUser(BlockedUser blockedUser);

    ResponseEntity<List<ParsBlockedUser>> getAllBlockedUsers();

    void delete(BlockedUser blockedUser);

    BlockedUser findById(Long userId);

    ResponseEntity<Void> delAdminReview(Long idReview, String username);

    ResponseEntity<Void> deletePost(String name, Long idPost);
}
