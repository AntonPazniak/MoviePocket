/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.repository.user;

import com.moviePocket.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserAuntByEmail(String mail);

    Optional<User> findUserByEmail(String email);

    User findByEmail(String mail);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByUsernameAndAccountActive(String username, boolean isActive);

    Optional<User> findUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :partialUsername, '%'))")
    List<User> findByPartialUsername(@Param("partialUsername") String partialUsername);

}
