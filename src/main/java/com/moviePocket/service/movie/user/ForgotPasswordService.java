package com.moviePocket.service.movie.user;

import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;

public interface ForgotPasswordService {

    ResponseEntity<Void> resetPassword(String token, String password);

    ResponseEntity<Void> createPasswordToken(String email) throws MessagingException;
}
