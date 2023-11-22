package com.moviePocket.repository.user;

import com.moviePocket.entities.user.ForgotPassword;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {


    void deleteAllByUser(User user);

    ForgotPassword findByTokenForgotPassword(String token);

    ForgotPassword findByUser(User user);

}
