package com.moviePocket.controller.user;

import com.moviePocket.controller.dto.UserRegistrationDto;
import com.moviePocket.entities.user.User;
import com.moviePocket.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Api(value = "Login and Registration Controller", description = "Controller for user login/registration")
public class LoginController {

    private final UserService userService;
    @RequestMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        UserRegistrationDto user = new UserRegistrationDto();
        model.addAttribute("user", user);
        return "registration";
    }

    @ApiOperation(value = "Register a user ", notes = "Registration with username, password(with validation) and email, email and username should be unique, cookie based")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully registered"),
            @ApiResponse(code = 403, message = "User already registered"),
            @ApiResponse(code = 400, message = "Password does not match the criteria"),
            @ApiResponse(code = 404, message = "Username or email is empty"),
            @ApiResponse(code = 401, message = "Username is already occupied")


    })
    @PostMapping("/registration")
    public ResponseEntity<?> registration(
            @Valid @ModelAttribute("user") UserRegistrationDto userDto, BindingResult result) throws MessagingException {
        User existingUser = userService.findUserByUsername(userDto.getUsername());
        User existingUserByMail = userService.findUserByEmail(userDto.getEmail());

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

    @ApiOperation(value = "Activate a user by email", notes = "Link is sent to the email after registration, returns whether user is activated(confirmed his/her mail or not ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User is activated"),
            @ApiResponse(code = 409, message = "User activation code is not found"),
    })
    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@RequestParam("token") String token) {
        return userService.activateUser(token);
    }

    @GetMapping("/registration/exist/username")
    public ResponseEntity<Boolean> existsUserByUsername(@RequestParam("username") String username) {
        return userService.existsByUsername(username);
    }

    @GetMapping("/registration/exist/email")
    public ResponseEntity<Boolean> existsUserByEmail(@RequestParam("email") String email) {
        return userService.existsByEmail(email);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Clear authentication
            SecurityContextHolder.getContext().setAuthentication(null);

            // Invalidate HttpSession (if any)
            request.getSession().invalidate();

            // Remove any authentication-related cookies
            removeAuthenticationCookies(request, response);
        }

        return ResponseEntity.ok(true);
    }

    private void removeAuthenticationCookies(HttpServletRequest request, HttpServletResponse response) {
        // You need to provide the names of your authentication-related cookies
        String[] cookieNames = {"yourCookieName1", "yourCookieName2"};

        for (String cookieName : cookieNames) {
            // Remove the cookie by setting its value to an empty string and setting the maxAge to 0
            response.addCookie(new Cookie(cookieName, ""));
        }
    }

}
