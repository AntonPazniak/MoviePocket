package com.moviePocket.service.impl.user;

import com.moviePocket.entities.user.ForgotPassword;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.user.ForgotPasswordRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.movie.user.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.UUID;

import static com.moviePocket.util.BuildEmail.buildEmail;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;

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

    private void sendEmail(User user, String token) throws MessagingException {
        String username = user.getUsername();
        String link = "http://localhost:3000/newPassword?token=" + token;
        String massage = "You are just in the middle of having your new password. \n Please confirm your email address by going by the link.";
        emailSenderService.sendMailWithAttachment(user.getEmail(), buildEmail(username, massage, link), "Password Recovery");

    }

}
