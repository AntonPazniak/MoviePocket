package com.moviePocket.controller.user;

import com.moviePocket.controller.dto.UserDto;
import com.moviePocket.entities.user.User;
import com.moviePocket.service.inter.image.ImageService;
import com.moviePocket.service.inter.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;


@Controller
@RestController
@RequestMapping("/user/edit")
@Api(value = "User edition controller", tags = "Bio, username, email or password edition")
public class UserEditController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/getUserDto")
    public ResponseEntity<UserDto> getUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(authentication.getName());
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

    @ApiOperation(value = "Delete a user", notes = "User set status deleted and stays in db")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully deleted the user"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.deleteUser(authentication.getName(), password);
    }

    @ApiOperation("Set a new password(password is validated")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully set the new password"),
            @ApiResponse(code = 400, message = "Password does not match the criteria"),

    })
    @PostMapping("/newPas")
    public ResponseEntity<Void> newPasswordPostForm(
            @RequestParam("passwordold") String passwordOld,
            @RequestParam("password0") String passwordNew0,
            @RequestParam("password1") String passwordNew1) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.setNewPassword(authentication.getName(), passwordOld, passwordNew0, passwordNew1);
    }

    @PostMapping("/newEmail")
    @ApiOperation("Set a new email")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully set the new email"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<Void> newEmailGetForm(@RequestParam("email") String email) throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.setTokenEmail(authentication.getName(), email);
    }

    @GetMapping("/activateNewEmail/{token}")
    @ApiOperation("Activate a new email")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully activated the new email")
    })
    public ResponseEntity<Void> activate(@PathVariable String token) {
        return userService.activateNewEmail(token);
    }

    @PostMapping("/newUsername")
    @ApiOperation(value = "Set a new username", notes = "username should be unique and not empty")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully set the new username"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<Void> newSetNewUsername(@RequestParam("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.setNewUsername(authentication.getName(), username);
    }


    @ApiOperation(value = "Set a new bio", notes = "username should be not empty")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully set the new username"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @PostMapping("/newBio")
    public ResponseEntity<Void> newSetNewBio(@RequestParam("bio") String bio) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.setNewBio(authentication.getName(), bio);
    }


    @PostMapping("/newAvatar")
    @ApiOperation("Set a new avatar")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully set the new avatar"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<Void> setNewAvatar(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.setNewAvatar(authentication.getName(), file);
    }


    @PostMapping("/deleteAvatar")
    @ApiOperation("Delete current avatar")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully deleted avatar"),
            @ApiResponse(code = 400, message = "Avatar is null"),
            @ApiResponse(code = 401, message = "User is not authorized")
    })
    public ResponseEntity<Void> deleteAvatar(@RequestParam("avatarId") Long avatarId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.deleteAvatar(authentication.getName(), avatarId);
    }
}
