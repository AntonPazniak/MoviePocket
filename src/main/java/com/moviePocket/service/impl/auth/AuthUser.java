package com.moviePocket.service.impl.auth;


import com.moviePocket.db.entities.user.User;
import com.moviePocket.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthUser {

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            throw new UnauthorizedException("You are not logged in.");
        }
        return (User) authentication.getPrincipal();
    }

}
