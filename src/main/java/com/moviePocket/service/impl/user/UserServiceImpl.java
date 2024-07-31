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
import com.moviePocket.db.entities.user.AccountActivate;
import com.moviePocket.db.entities.user.ForgotPassword;
import com.moviePocket.db.entities.user.NewEmail;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.user.AccountActivateRepository;
import com.moviePocket.db.repository.user.ForgotPasswordRepository;
import com.moviePocket.db.repository.user.NewEmailRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.exception.BadRequestException;
import com.moviePocket.exception.ForbiddenException;
import com.moviePocket.exception.UnauthorizedException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.image.ImageServiceImpl;
import com.moviePocket.service.inter.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
    public void deleteUser(String email, String password) {
        User user = chekUserAuntByEmail(email);
        if (user == null) {
            throw new UnauthorizedException("User with email " + email + " not found.");
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
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
    public void setNewPassword(String username, String oldPassword, String newPassword) {
        User user = findUserByUsername(username);
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
        if (user.getUsername().equals(newUsername)) {
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


    public ResponseEntity<Void> setNewAvatar(String email, MultipartFile file) {
        User user = userRepository.findByEmail(email);
        ImageEntity lastImage = null;

        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (file.getSize() > 7340032) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (user.getAvatar() != null)
                lastImage = user.getAvatar();
            ImageEntity imageEntity = imageService.resizeImage(file);
            user.setAvatar(imageEntity);
            userRepository.save(user);

            if (lastImage != null) {
                imageService.delImage(lastImage);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<Void> deleteAvatar(String email, Long imageId) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (user.getAvatar() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ImageEntity image = user.getAvatar();

        user.setAvatar(null);
        userRepository.save(user);

        imageService.deleteImage(image.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> setTokenEmail(String email, String newEmail) throws MessagingException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (email.equals(newEmail))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
            NewEmail newEmailOb = newEmailRepository.findByUser(user);
            if (newEmailOb == null) {
                newEmailOb = new NewEmail(user, newEmail, UUID.randomUUID().toString());
                newEmailRepository.save(newEmailOb);
            } else {
                newEmailOb.setNewEmail(newEmail);
                newEmailRepository.save(newEmailOb);
            }

            String username = user.getUsername();
            String link = "https://moviepocket.projektstudencki.pl/api/user/edit/activateNewEmail/" + newEmailOb.getTokenEmailActivate();
            String massage = "You are just in the middle of setting up your new email address. \n Please confirm your new email address.";

            emailSenderService.sendMailWithAttachment(newEmailOb.getNewEmail(), buildEmail(username, massage, link), "New Mail Confirmation");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> activateNewEmail(String token) {
        NewEmail newEmail = newEmailRepository.findByTokenEmailActivate(token);
        if (newEmail == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            User user = newEmail.getUser();
            user.setEmail(newEmail.getNewEmail());
            newEmailRepository.delete(newEmail);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @Override
    public ResponseEntity<Void> activateUser(String token) {
        AccountActivate accountActivate = accountActivateRepository.findByTokenAccountActivate(token);
        if (accountActivate != null) {
            User user = accountActivate.getUser();
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                user.setEmailVerification(Boolean.TRUE);
                userRepository.save(user);
                accountActivateRepository.delete(accountActivate);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<Void> createPasswordToken(String email) throws MessagingException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            ForgotPassword forgotPassword = forgotPasswordRepository.findByUser(user);
            if (forgotPassword != null) {
                forgotPassword.setTokenForgotPassword(UUID.randomUUID().toString());
            } else {
                forgotPassword = new ForgotPassword(user, UUID.randomUUID().toString());
            }
            forgotPasswordRepository.save(forgotPassword);
            sendEmail(user, forgotPassword.getTokenForgotPassword());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Void> resetPassword(String token, String password) {
        ForgotPassword forgotPassword = forgotPasswordRepository.findByTokenForgotPassword(token);
        if (forgotPassword == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            User user = forgotPassword.getUser();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            forgotPasswordRepository.deleteAllByUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<List<UserPostDto>> findByPartialUsername(String username) {
        if (username.isEmpty())
            return ResponseEntity.ok(null);
        List<User> users = userRepository.findByPartialUsername(username);
        if (users == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<UserPostDto> userPostDtos = users.stream()
                .map(user -> new UserPostDto(user.getUsername(), user.getAvatar() != null ? user.getAvatar().getId() : null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userPostDtos);
    }

    private void sendEmail(User user, String token) throws MessagingException {
        String username = user.getUsername();
        String link = "https://moviepocket.projektstudencki.pl/newPassword?token=" + token;
        String massage = "You are just in the middle of having your new password. \n Please confirm your email address by going by the link.";
        emailSenderService.sendMailWithAttachment(user.getEmail(), buildEmail(username, massage, link), "Password Recovery");

    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {

//        User user = userRepository.findByEmail(usernameOrEmail);
//        if (user != null) {
//            if (user.getEmailVerification()) {
//                return new org.springframework.security.core.userdetails.User(user.getEmail()
//                        , user.getPassword(),
//                        user.getRoles().stream()
//                                .map((role) -> new SimpleGrantedAuthority(role.getName()))
//                                .collect(Collectors.toList()));
//            } else {
//                throw new UsernameNotFoundException("You have not verified your email");
//            }
//        } else {
//            throw new UsernameNotFoundException("Invalid email or password");
//
//        }
        return null;
    }

    @Override
    public User chekUserAuntByEmail(String email) {
        return userRepository.findUserAuntByEmail(email).orElseThrow(() -> new UnauthorizedException("You are not logged in"));
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result);
        return result;
    }

    public ResponseEntity<Boolean> existsByUsername(String username) {
        return ResponseEntity.ok(userRepository.existsByUsername(username));
    }

    public ResponseEntity<Boolean> existsByEmail(String email) {
        return ResponseEntity.ok(userRepository.existsByEmail(email));
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("You are not logged in"));
    }


}
