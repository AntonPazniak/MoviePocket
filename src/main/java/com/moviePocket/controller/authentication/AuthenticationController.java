package com.moviePocket.controller.authentication;


import com.moviePocket.controller.dto.auth.AuthenticationRequest;
import com.moviePocket.controller.dto.auth.AuthenticationResponse;
import com.moviePocket.controller.dto.auth.RegisterRequest;
import com.moviePocket.exception.BadRequestException;
import com.moviePocket.service.impl.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> {
                if (error.getField().equals("email")) {
                    throw new BadRequestException("Invalid email format");
                } else if (error.getField().equals("password")) {
                    throw new BadRequestException("Password does not meet the required criteria");
                }
            });
        }
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        var respon = ResponseEntity.ok(service.authenticate(request));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(respon.toString());
        log.info(authentication.toString());
        return respon;
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}