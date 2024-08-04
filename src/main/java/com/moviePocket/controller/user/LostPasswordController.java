/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.user;

import com.moviePocket.security.validation.ValidPassword;
import com.moviePocket.service.inter.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lostpassword")
@RequiredArgsConstructor
//@Validated
//TODO password validation for lost password
public class LostPasswordController {

    private final UserService userService;

    @PostMapping("/setEmail")
    public ResponseEntity<Void> setMail(@RequestParam("email") String email) {
        userService.createPasswordToken(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetPassword(@RequestParam("token") String token, @RequestParam("password") @ValidPassword String password) {
        userService.resetPassword(token, password);
        return ResponseEntity.ok().build();
    }

}