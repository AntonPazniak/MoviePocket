package com.moviePocket.controller.user;

import com.moviePocket.security.validation.ValidPassword;
import com.moviePocket.service.movie.user.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/forgotpassword")

//@Validated
//TODO password validation for lost password
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/set")
    public ResponseEntity<Void> setEmail(@RequestParam("email") String email) throws MessagingException {
        return forgotPasswordService.createPasswordToken(email);
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(@RequestParam("token") String token, @RequestParam("password") @ValidPassword String password) {
        return forgotPasswordService.resetPassword(token, password);
    }

}