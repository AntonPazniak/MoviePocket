package com.moviePocket.controller.dto.auth;


import com.moviePocket.security.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    @Email
    private String email;
    @ValidPassword
    private String password;

}