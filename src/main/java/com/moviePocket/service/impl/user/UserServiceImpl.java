/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.user;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.db.entities.image.ImageEntity;
import com.moviePocket.db.entities.user.ForgotPassword;
import com.moviePocket.db.entities.user.NewEmail;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.user.AccountActivateRepository;
import com.moviePocket.db.repository.user.ForgotPasswordRepository;
import com.moviePocket.db.repository.user.NewEmailRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.exception.BadRequestException;
import com.moviePocket.exception.ForbiddenException;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.exception.UnauthorizedException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.image.ImageServiceImpl;
import com.moviePocket.service.inter.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.moviePocket.util.BuildEmail.buildEmail;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final AccountActivateRepository accountActivateRepository;
    private final NewEmailRepository newEmailRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final ImageServiceImpl imageService;
    private final AuthUser authUser;

    @Override
    public void cleanSave(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not exist"));
    }

    @Override
    public void deleteUser(String password) {
        User user = authUser.getAuthenticatedUser();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ForbiddenException("Incorrect password.");
        } else {
            user.setAccountActive(false);
            user.setEmailVerification(false);
            user.setEmail(user.getEmail() + " not active " + user.getId());
            user.setPassword("");
            user.setUsername(String.valueOf(user.getId()));
            userRepository.save(user);
        }
    }


    @Override
    public void setNewPassword(String oldPassword, String newPassword) {
        User user = authUser.getAuthenticatedUser();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ForbiddenException("Incorrect old password.");
        } else if (!oldPassword.equals(newPassword)) {
            throw new BadRequestException("The current password is the same as the new one.");
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    @Override
    public void setNewUsername(String newUsername) {
        var user = authUser.getAuthenticatedUser();
        if (user.getLogin().equals(newUsername)) {
            throw new BadRequestException("The current username is the same as the new one.");
        } else if (userRepository.existsByUsername(newUsername)) {
            throw new ForbiddenException("Username is already taken.");
        } else {
            user.setUsername(newUsername);
            userRepository.save(user);
        }
    }


    @Override
    public void setNewBio(String bio) {
        var user = authUser.getAuthenticatedUser();
        user.setBio(bio);
        userRepository.save(user);
    }

    @Override
    public void setNewAvatar(MultipartFile file) {
        User user = authUser.getAuthenticatedUser();
        Optional<ImageEntity> lastImage = Optional.ofNullable(user.getAvatar());

        if (file.getSize() > 7340032) {
            throw new BadRequestException("The image is too large.");
        } else {
            ImageEntity imageEntity = imageService.resizeImage(file);
            user.setAvatar(imageEntity);
            userRepository.save(user);

            lastImage.ifPresent(imageService::delImage);
        }
    }

    @Override
    public void deleteAvatar() {
        User user = authUser.getAuthenticatedUser();

        ImageEntity image = user.getAvatar();

        user.setAvatar(null);
        userRepository.save(user);

        imageService.deleteImage(image.getId());
    }

    public void setTokenEmail(String email, String newEmail) throws MessagingException {
        User user = userRepository.findByEmail(email);
        var token = UUID.randomUUID().toString();
        if (email.equals(newEmail))
            throw new ForbiddenException("Rejected");
        else {
            var newEmailOb = newEmailRepository.findByUser(user);
            if (newEmailOb.isEmpty()) {
                newEmailRepository.save(NewEmail.builder()
                        .user(user)
                        .newEmail(email)
                        .tokenEmailActivate(token)
                        .build());
            } else {
                newEmailOb.get().setNewEmail(newEmail);
                newEmailRepository.save(newEmailOb.get());
            }

            String username = user.getLogin();
            String link = "https://moviepocket.projektstudencki.pl/api/user/edit/activateNewEmail/" + token;
            String massage = "You are just in the middle of setting up your new email address. \n Please confirm your new email address.";

            emailSenderService.sendMailWithAttachment(newEmail, buildEmail(username, massage, link), "New Mail Confirmation");
        }
    }

    @Override
    public void activateNewEmail(String token) {
        var newEmail = newEmailRepository.findByTokenEmailActivate(token)
                .orElseThrow(() -> new ForbiddenException("Rejected"));

        User user = newEmail.getUser();
        user.setEmail(newEmail.getNewEmail());
        newEmailRepository.delete(newEmail);
        userRepository.save(user);
    }

    @Override
    public void activateUser(String token) {
        var accountActivate = accountActivateRepository.findByTokenAccountActivate(token)
                .orElseThrow(() -> new ForbiddenException("Rejected"));
        User user = accountActivate.getUser();
        user.setEmailVerification(true);
        userRepository.save(user);
        accountActivateRepository.delete(accountActivate);
    }


    @SneakyThrows
    @Transactional
    public void createPasswordToken(String email) {
        var user = userRepository.findUserByEmail(email);
        var token = UUID.randomUUID().toString();
        if (user.isPresent()) {
            var forgotPassword = forgotPasswordRepository.findByUser(user.get());
            if (forgotPassword.isEmpty()) {
                forgotPasswordRepository.save(
                        ForgotPassword.builder()
                                .tokenForgotPassword(token)
                                .user(user.get())
                                .build()
                );
            } else {
                forgotPassword.get().setTokenForgotPassword(token);
                forgotPasswordRepository.save(forgotPassword.get());
            }
            sendEmail(user.get(), token);
        }
    }

    @Override
    public void resetPassword(String token, String password) {
        var forgotPassword = forgotPasswordRepository.findByTokenForgotPassword(token)
                .orElseThrow(() -> new BadRequestException("Invalid token."));
        User user = forgotPassword.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        forgotPasswordRepository.deleteAllByUser(user);
    }

    @Override
    public List<UserPostDto> findByPartialUsername(String username) {
        return userRepository.findByPartialUsername(username).stream()
                .map(user -> UserPostDto.builder()
                        .username(user.getLogin())
                        .avatar(user.getAvatar() != null ? user.getAvatar().getId() : null)
                        .build())
                .toList();
    }


    private void sendEmail(User user, String token) throws MessagingException {
        String username = user.getLogin();
        String link = "https://moviepocket.projektstudencki.pl/newPassword?token=" + token;
        String massage = "You are just in the middle of having your new password. \n Please confirm your email address by going by the link.";
        emailSenderService.sendMailWithAttachment(user.getEmail(), buildEmail(username, massage, link), "Password Recovery");

    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {

        User user = userRepository.findByEmail(usernameOrEmail);
        if (user != null) {
            if (user.getEmailVerification()) {
                return new org.springframework.security.core.userdetails.User(user.getEmail()
                        , user.getPassword(),
                        user.getRoles().stream()
                                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                                .collect(Collectors.toList()));
            }
        }
        throw new NotFoundException("Invalid email.");
    }

    @Override
    public User chekUserAuntByEmail(String email) {
        return userRepository.findUserAuntByEmail(email).orElseThrow(() -> new UnauthorizedException("You are not logged in"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("You are not logged in"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("You are not logged in"));
    }

}
