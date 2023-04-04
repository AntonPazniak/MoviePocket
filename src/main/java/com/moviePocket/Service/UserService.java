package com.moviePocket.Service;

import com.moviePocket.controller.dto.UserRegistrationDto;
import com.moviePocket.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void save(UserRegistrationDto registrationDto);

    User findUserByEmail(String email);

}