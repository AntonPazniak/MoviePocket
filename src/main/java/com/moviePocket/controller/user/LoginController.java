/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.user;

import com.moviePocket.service.inter.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Api(value = "Login and Registration Controller", description = "Controller for user login/registration")
public class LoginController {

    private final UserService userService;

    @RequestMapping("/login")
    public String loginForm() {
        return "login";
    }


    @ApiOperation(value = "Activate a user by email", notes = "Link is sent to the email after registration, returns whether user is activated(confirmed his/her mail or not ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User is activated"),
            @ApiResponse(code = 409, message = "User activation code is not found"),
    })
    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@RequestParam("token") String token) {
        return userService.activateUser(token);
    }


    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Clear authentication
            SecurityContextHolder.getContext().setAuthentication(null);

            // Invalidate HttpSession (if any)
            request.getSession().invalidate();

            // Remove any authentication-related cookies
            removeAuthenticationCookies(request, response);
        }

        return ResponseEntity.ok(true);
    }

    private void removeAuthenticationCookies(HttpServletRequest request, HttpServletResponse response) {
        // You need to provide the names of your authentication-related cookies
        String[] cookieNames = {"yourCookieName1", "yourCookieName2"};

        for (String cookieName : cookieNames) {
            // Remove the cookie by setting its value to an empty string and setting the maxAge to 0
            response.addCookie(new Cookie(cookieName, ""));
        }
    }

}
