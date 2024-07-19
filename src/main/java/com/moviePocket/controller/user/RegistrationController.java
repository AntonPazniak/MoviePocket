package com.moviePocket.controller.user;


import com.moviePocket.controller.dto.UserRegistrationDto;
import com.moviePocket.entities.user.User;
import com.moviePocket.service.inter.user.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Register a user ", notes = "Registration with username, password(with validation) and email, email and username should be unique, cookie based")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully registered"),
            @ApiResponse(code = 403, message = "User already registered"),
            @ApiResponse(code = 400, message = "Password does not match the criteria"),
            @ApiResponse(code = 404, message = "Username or email is empty"),
            @ApiResponse(code = 401, message = "Username is already occupied")


    })
    @PostMapping("/")
    public ResponseEntity<?> registration(
            @Valid @ModelAttribute("user") UserRegistrationDto userDto, BindingResult result) throws MessagingException {
        User existingUser = userService.findUserByUsername(userDto.getUsername());
        User existingUserByMail = userService.chekUserAuntByEmail(userDto.getEmail());

        if ((existingUser != null) && existingUser.isAccountActive()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //403
        if ((existingUser != null) || (existingUserByMail != null))
            return new ResponseEntity<>("User already registered !!!", HttpStatus.FORBIDDEN);

        //400
        if (result.hasFieldErrors("password")) {
            String passwordErrorMessage = result.getFieldError("password").getDefaultMessage();
            // Handle the password validation error, e.g., add a custom message to the response
            return new ResponseEntity<>(passwordErrorMessage, HttpStatus.BAD_REQUEST);
        }

        //404
        if (userDto.getUsername().isEmpty() || userDto.getEmail().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //404
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //201
        userService.save(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/exist/username")
    public ResponseEntity<Boolean> existsUserByUsername(@RequestParam("username") String username) {
        return userService.existsByUsername(username);
    }

    @GetMapping("/exist/email")
    public ResponseEntity<Boolean> existsUserByEmail(@RequestParam("email") String email) {
        return userService.existsByEmail(email);
    }

}
