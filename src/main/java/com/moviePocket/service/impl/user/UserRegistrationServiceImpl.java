package com.moviePocket.service.impl.user;


import com.moviePocket.controller.dto.auth.RegisterRequest;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.exception.BadRequestException;
import com.moviePocket.service.inter.user.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(@NotNull RegisterRequest user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("This username is already taken");
        } else if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("This email is already taken");
        } else {
            var newUser = User.builder()
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .username(user.getUsername())
                    .created(LocalDateTime.now())
                    .accountActive(true)
                    .emailVerification(true)
                    .build();
            userRepository.save(newUser);
            return newUser;
        }
    }


}
