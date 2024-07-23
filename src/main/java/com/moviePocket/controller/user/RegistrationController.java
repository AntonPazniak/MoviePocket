package com.moviePocket.controller.user;


//
//@RestController
//@RequestMapping("/registration")
//@RequiredArgsConstructor
//public class RegistrationController {
//
//    @Autowired
//    private final UserService userService;
//
//    @Operation(summary = "Register a user", description = "Registration with username, password(with validation) and email, email and username should be unique, cookie based")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Successfully registered"),
//            @ApiResponse(responseCode = "403", description = "User already registered"),
//            @ApiResponse(responseCode = "400", description = "Password does not match the criteria"),
//            @ApiResponse(responseCode = "404", description = "Username or email is empty"),
//            @ApiResponse(responseCode = "401", description = "Username is already occupied")
//    })
//    @PostMapping("/")
//    public ResponseEntity<?> registration(
//            @Valid @ModelAttribute("user") UserRegistrationDto userDto, BindingResult result) throws MessagingException {
//        User existingUser = userService.findUserByUsername(userDto.getUsername());
//        User existingUserByMail = userService.chekUserAuntByEmail(userDto.getEmail());
//
//        if ((existingUser != null) && existingUser.isAccountActive()) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//
//        if ((existingUser != null) || (existingUserByMail != null))
//            return new ResponseEntity<>("User already registered !!!", HttpStatus.FORBIDDEN);
//
//        if (result.hasFieldErrors("password")) {
//            String passwordErrorMessage = result.getFieldError("password").getDefaultMessage();
//            return new ResponseEntity<>(passwordErrorMessage, HttpStatus.BAD_REQUEST);
//        }
//
//        if (userDto.getUsername().isEmpty() || userDto.getEmail().isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        if (result.hasErrors()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        userService.save(userDto);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
//    @GetMapping("/exist/username")
//    public ResponseEntity<Boolean> existsUserByUsername(@RequestParam("username") String username) {
//        return userService.existsByUsername(username);
//    }
//
//    @GetMapping("/exist/email")
//    public ResponseEntity<Boolean> existsUserByEmail(@RequestParam("email") String email) {
//        return userService.existsByEmail(email);
//    }
//}
