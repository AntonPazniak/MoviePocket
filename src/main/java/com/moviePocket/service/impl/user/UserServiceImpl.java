package com.moviePocket.service.impl.user;

import com.moviePocket.controller.dto.UserRegistrationDto;
import com.moviePocket.entities.user.Role;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.user.RoleRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.movie.user.UserService;
import com.moviePocket.util.TbConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.moviePocket.util.BuildEmail.buildEmail;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailSenderService emailSenderService;

    @Override
    public void save(UserRegistrationDto userDto) throws MessagingException {
        Role role = roleRepository.findByName(TbConstants.Roles.USER);

        if (role == null)
            role = roleRepository.save(new Role(TbConstants.Roles.USER));
        User user = new User(userDto.getUsername(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
                Arrays.asList(role), UUID.randomUUID().toString());
        userRepository.save(user);

        String username = user.getUsername();
        String link = "http://localhost:3000/activateUser?token=" + user.getActivationCode();
        String massage = "Welcome to MoviePocket family. We really hope that you will enjoy being a part of MoviePocket family \n" +
                " We want to make sure it's really you. To do that please confirm your mail by clicking the link below.";

        emailSenderService.sendMailWithAttachment(user.getEmail(), buildEmail(username, massage, link), "Email Verification");

    }

    @Override
    public ResponseEntity<Void> deleteUser(String email, String pas) {
        User user = findUserByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!passwordEncoder.matches(pas, user.getPassword()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else {
            user.setAccountActive(false);
            user.setEmailVerification(false);
            user.setEmail(user.getEmail() + "not active" + user.getId());
            user.setPassword("");
            user.setUsername(String.valueOf(user.getId()));
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
    public ResponseEntity<Void> setNewPassword(String email, String passwordOld, String passwordNew0, String passwordNew1) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (!passwordEncoder.matches(passwordOld, user.getPassword()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        else if (!passwordNew0.equals(passwordNew1)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(passwordEncoder.encode(passwordNew0));
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<Void> setNewUsername(String email, String username) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (userRepository.existsByUsername(username) && user.isAccountActive() || username.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            user.setUsername(username);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<Void> setNewBio(String email, String bio) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (bio.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
            user.setBio(bio);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<Void> setTokenEmail(String email, String newEmail) throws MessagingException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if (email.equals(newEmail))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
            user.setNewEmail(newEmail);
            user.setNewEmailToken(UUID.randomUUID().toString());
            userRepository.save(user);

            String username = user.getUsername();
            String link = "http://localhost:8080/user/edit/activateNewEmail/" + user.getNewEmailToken();
            String massage = "You are just in the middle of setting up your new email address. \n Please confirm your new email address.";

            emailSenderService.sendMailWithAttachment(user.getNewEmail(), buildEmail(username, massage, link), "New Mail Confirmation");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Void> activateNewEmail(String token) {
        User user = userRepository.findByNewEmailToken(token);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            user.setEmail(user.getNewEmail());
            user.setNewEmail(null);
            user.setNewEmailToken(null);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @Override
    public ResponseEntity<Void> activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            System.out.println("fuck");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            System.out.println(user);
            user.setEmailVerification(Boolean.TRUE);
            user.setActivationCode(null);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(usernameOrEmail);
        if (user != null) {
            if (user.getEmailVerification()) {
                return new org.springframework.security.core.userdetails.User(user.getEmail()
                        , user.getPassword(),
                        user.getRoles().stream()
                                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                                .collect(Collectors.toList()));
            } else {
                throw new UsernameNotFoundException("You have not verified your email");
            }
        } else {
            throw new UsernameNotFoundException("Invalid email or password");

        }
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result);
        return result;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsernameAndAccountActive(username, true);
    }

    public ResponseEntity<Boolean> existsByUsername(String username) {
        return ResponseEntity.ok(userRepository.existsByUsername(username));
    }

    public ResponseEntity<Boolean> existsByEmail(String email) {
        return ResponseEntity.ok(userRepository.existsByEmail(email));
    }


}
