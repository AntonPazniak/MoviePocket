/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.user;

import com.moviePocket.controller.dto.UserDto;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.service.inter.image.ImageService;
import com.moviePocket.service.inter.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/edit")
@Tag(name = "User edition controller", description = "Bio, username, email or password edition")
public class UserEditController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/getUserDto")
    public ResponseEntity<UserDto> getUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.chekUserAuntByEmail(authentication.getName());
        if (user != null) {
            Long idAvatar = null;
            if (user.getAvatar() != null)
                idAvatar = user.getAvatar().getId();
            return ResponseEntity.ok(new UserDto(
                    user.getUsername(),
                    user.getEmail(),
                    user.getBio(),
                    idAvatar
            ));
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Delete a user", description = "User set status deleted and stays in db")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted the user"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/delete")
    public void deleteUser(@RequestParam String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteUser(authentication.getName(), password);
    }

    @Operation(summary = "Set a new password", description = "Password is validated")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully set the new password"),
            @ApiResponse(responseCode = "400", description = "Password does not match the criteria"),
    })
    @PostMapping("/newPas")
    public void newPasswordPostForm(
            @RequestParam("passwordold") String passwordOld,
            @RequestParam("password0") String passwordNew0,
            @RequestParam("password1") String passwordNew1) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.setNewPassword(authentication.getName(), passwordOld, passwordNew0, passwordNew1);
    }

    @Operation(summary = "Set a new email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully set the new email"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/newEmail")
    public void newEmailGetForm(@RequestParam("email") String email) throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.setTokenEmail(authentication.getName(), email);
    }

    @Operation(summary = "Activate a new email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully activated the new email")
    })
    @GetMapping("/activateNewEmail/{token}")
    public ResponseEntity<Void> activate(@PathVariable String token) {
        return userService.activateNewEmail(token);
    }

    @Operation(summary = "Set a new username", description = "Username should be unique and not empty")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully set the new username"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/newUsername")
    public void newSetNewUsername(@RequestParam("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.setNewUsername(authentication.getName(), username);
    }

    @Operation(summary = "Set a new bio", description = "Bio should not be empty")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully set the new bio")
    })
    @PostMapping("/newBio")
    public void newSetNewBio(@RequestParam("bio") String bio) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.setNewBio(authentication.getName(), bio);
    }

    @Operation(summary = "Set a new avatar")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully set the new avatar"),
            @ApiResponse(responseCode = "400", description = "Image size is exceeded (1MB)"),
            @ApiResponse(responseCode = "401", description = "User is not authorized")
    })
    @PostMapping("/newAvatar")
    public ResponseEntity<Void> setNewAvatar(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.setNewAvatar(authentication.getName(), file);
    }

    @Operation(summary = "Delete current avatar")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully deleted avatar"),
            @ApiResponse(responseCode = "400", description = "Avatar is null"),
            @ApiResponse(responseCode = "401", description = "User is not authorized")
    })
    @PostMapping("/deleteAvatar")
    public ResponseEntity<Void> deleteAvatar(@RequestParam("avatarId") Long avatarId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.deleteAvatar(authentication.getName(), avatarId);
    }
}
