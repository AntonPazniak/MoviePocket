package com.moviePocket.service.movie.user;

import com.moviePocket.controller.dto.UserRegistrationDto;
import com.moviePocket.entities.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.mail.MessagingException;

public interface UserService extends UserDetailsService {
    void save(UserRegistrationDto registrationDto) throws MessagingException;

    User findById(Long id);

    User findUserByEmail(String email);

    ResponseEntity<Void> activateUser(String code);

    ResponseEntity<Void> setNewPassword(String email, String passwordOld, String passwordNew0, String passwordNew1);

    ResponseEntity<Void> deleteUser(String email, String pas);

    ResponseEntity<Void> setTokenEmail(String oldEmail, String newEmail) throws MessagingException;

    ResponseEntity<Void> activateNewEmail(String token);

    ResponseEntity<Void> setNewUsername(String email, String username);

    ResponseEntity<Void> setNewBio(String email, String bio);

    User findUserByUsername(String username);

    ResponseEntity<Boolean> existsByUsername(String username);

    ResponseEntity<Boolean> existsByEmail(String email);

    ResponseEntity<Void> createPasswordToken(String email) throws MessagingException;

    ResponseEntity<Void> resetPassword(String token, String password);

}
