package com.moviePocket.service.inter.admin;

import com.moviePocket.entities.user.BlockedUser;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BlockedUserService {
    BlockedUser saveBlockedUser(BlockedUser blockedUser);

    List<BlockedUser> getAllBlockedUsers();

    void delete(BlockedUser blockedUser);

    BlockedUser findById(Long userId);

    ResponseEntity<Void> delAdminReview(Long idReview, String username);

    ResponseEntity<Void> deletePost(String name, Long idPost);
}