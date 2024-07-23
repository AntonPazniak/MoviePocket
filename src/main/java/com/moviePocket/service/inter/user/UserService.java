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
import com.moviePocket.controller.dto.auth.RegisterRequest;
import com.moviePocket.db.entities.user.User;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {
    User saveNewUser(RegisterRequest registrationDto) throws MessagingException, MessagingException;

    void cleanSave(User user);

    User findById(Long id);

    User chekUserAuntByEmail(String email);

    ResponseEntity<Void> activateUser(String code);

    void setNewPassword(String email, String passwordOld, String passwordNew0, String passwordNew1) throws BadRequestException;

    void deleteUser(String email, String pas);

    ResponseEntity<Void> setTokenEmail(String oldEmail, String newEmail) throws MessagingException;

    ResponseEntity<Void> activateNewEmail(String token);

    void setNewUsername(String email, String username);

    void setNewBio(String email, String bio);

    ResponseEntity<Void> setNewAvatar(String email, MultipartFile file);

    ResponseEntity<Void> deleteAvatar(String email, Long imageId);

    User findUserByUsername(String username);

    ResponseEntity<Boolean> existsByUsername(String username);

    ResponseEntity<Boolean> existsByEmail(String email);

    ResponseEntity<Void> createPasswordToken(String email) throws MessagingException;

    ResponseEntity<Void> resetPassword(String token, String password);

    ResponseEntity<List<UserPostDto>> findByPartialUsername(String username);

}
