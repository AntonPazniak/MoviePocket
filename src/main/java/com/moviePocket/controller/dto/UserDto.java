package com.moviePocket.controller.dto;

import com.moviePocket.entities.user.User;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String email;
    private String bio;
    private Long avatar;


    public UserDto(String username, String email, String bio, Long avatar) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.avatar = avatar;
    }

    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setBio(bio);

        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setBio(user.getBio());
        userDto.setAvatar(user.getAvatar().getId());
        return userDto;
    }
}

