/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.user;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.db.entities.user.User;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {

    void cleanSave(User user);

    User findById(Long id);

    User chekUserAuntByEmail(String email);

    void activateUser(String code);

    void setNewPassword(String oldPassword, String newPassword);

    void deleteUser(String pas);

    void setTokenEmail(String oldEmail, String newEmail) throws MessagingException;

    void activateNewEmail(String token);

    void setNewUsername(String username);

    void setNewBio(String bio);

    void setNewAvatar(MultipartFile file);

    void deleteAvatar();

    User findUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void createPasswordToken(String email);

    void resetPassword(String token, String password);

    List<UserPostDto> findByPartialUsername(String username);

}
