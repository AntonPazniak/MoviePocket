package com.moviePocket.controller.admin;

import com.moviePocket.entities.user.BlockedUser;
import com.moviePocket.entities.user.User;
import com.moviePocket.service.inter.admin.BlockedUserService;
import com.moviePocket.service.inter.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final BlockedUserService blockedUserService;
    private final UserService userService;


    @PostMapping("/banUser/{userId}")
    public ResponseEntity<Void> banUser(@PathVariable Long userId, @RequestParam String comment) {
        User admin = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userService.findById(userId);
        if (admin.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (blockedUserService.findById(userId) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            user.setAccountActive(false);
            userService.cleanSave(user);

            BlockedUser blockedUser = new BlockedUser();
            blockedUser.setUser(user);
            blockedUser.setComment(comment);
            blockedUserService.saveBlockedUser(blockedUser);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/unbanUser/{userId}")
    public ResponseEntity<Void> unbanUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        User admin = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (admin.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            user.setAccountActive(true);
            userService.cleanSave(user);

            BlockedUser blockedUser = blockedUserService.findById(userId);

            if (blockedUser == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            blockedUserService.delete(blockedUser);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/blockedUsers")
    public ResponseEntity<List<BlockedUser>> getBlockedUsers() {
        List<BlockedUser> blockedUsers = blockedUserService.getAllBlockedUsers();
        return ResponseEntity.ok(blockedUsers);
    }

    @PostMapping("/delReview")
    public ResponseEntity<Void> delMovieReview(@RequestParam("idReview") Long idReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return blockedUserService.delAdminReview(idReview, authentication.getName());
    }
}
