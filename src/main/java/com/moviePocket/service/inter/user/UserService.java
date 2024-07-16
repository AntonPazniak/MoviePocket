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
import com.moviePocket.controller.dto.UserRegistrationDto;
import com.moviePocket.entities.user.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {
    void save(UserRegistrationDto registrationDto) throws MessagingException, MessagingException;

    void cleanSave(User user);

    User findById(Long id);

    User findUserByEmail(String email);

    ResponseEntity<Void> activateUser(String code);

    ResponseEntity<Void> setNewPassword(String email, String passwordOld, String passwordNew0, String passwordNew1);

    ResponseEntity<Void> deleteUser(String email, String pas);

    ResponseEntity<Void> setTokenEmail(String oldEmail, String newEmail) throws MessagingException;

    ResponseEntity<Void> activateNewEmail(String token);

    ResponseEntity<Void> setNewUsername(String email, String username);

    ResponseEntity<Void> setNewBio(String email, String bio);

    ResponseEntity<Void> setNewAvatar(String email, MultipartFile file);

    ResponseEntity<Void> deleteAvatar(String email, Long imageId);

    User findUserByUsername(String username);

    ResponseEntity<Boolean> existsByUsername(String username);

    ResponseEntity<Boolean> existsByEmail(String email);

    ResponseEntity<Void> createPasswordToken(String email) throws MessagingException;

    ResponseEntity<Void> resetPassword(String token, String password);

    ResponseEntity<List<UserPostDto>> findByPartialUsername(String username);

}
