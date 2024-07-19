/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.repository.user;

import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserAuntByEmail(String mail);

    User findByEmail(String mail);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByUsernameAndAccountActive(String username, boolean isActive);

    User findByUsername(String username);

    @Query("SELECT u FROM users u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :partialUsername, '%'))")
    List<User> findByPartialUsername(@Param("partialUsername") String partialUsername);

}
