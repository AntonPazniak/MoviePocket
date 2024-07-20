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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Login and Registration Controller", description = "Controller for user login/registration")
public class LoginController {

    private final UserService userService;

    @RequestMapping("/login")
    public String loginForm() {
        return "login";
    }

    @Operation(summary = "Activate a user by email", description = "Link is sent to the email after registration, returns whether user is activated (confirmed his/her mail or not)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is activated"),
            @ApiResponse(responseCode = "409", description = "User activation code is not found")
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
            Cookie cookie = new Cookie(cookieName, "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
