package com.moviePocket.repository.admin;

import com.moviePocket.entities.user.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {
    BlockedUser findByUser_Id(Long userId);
}
