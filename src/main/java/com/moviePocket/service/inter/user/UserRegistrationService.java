package com.moviePocket.service.inter.user;

import com.moviePocket.controller.dto.auth.RegisterRequest;
import com.moviePocket.db.entities.user.User;
import org.jetbrains.annotations.NotNull;

public interface UserRegistrationService {

    User registerUser(@NotNull RegisterRequest user);

}
