/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.admin;

import com.moviePocket.entities.user.BlockedUser;
import com.moviePocket.entities.user.ParsBlockedUser;
import com.moviePocket.entities.user.User;
import com.moviePocket.service.inter.admin.BlockedUserService;
import com.moviePocket.service.inter.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Ban user by admin", description = "Make user status unactive and add to Blocked table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created new post"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not admin"),
            @ApiResponse(responseCode = "404", description = "Not found - no such a user"),
            @ApiResponse(responseCode = "400", description = "Bad request - user is already blocked and is in table")
    })
    @PostMapping("/banUser/{username}")
    public ResponseEntity<Void> banUser(@PathVariable String username, @RequestParam String comment) {
        User admin = userService.chekUserAuntByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userService.findUserByUsername(username);
        if (admin.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (blockedUserService.findById(user.getId()) != null) {
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

    @Operation(summary = "Unban user by admin", description = "Make user status active and delete from Blocked table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created new post"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not admin"),
            @ApiResponse(responseCode = "404", description = "Not found - no such a user"),
            @ApiResponse(responseCode = "400", description = "Bad request - user is already unblocked and is in table")
    })
    @PostMapping("/unbanUser/{username}")
    public ResponseEntity<Void> unbanUser(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        User admin = userService.chekUserAuntByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (admin.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (blockedUserService.findById(user.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            user.setAccountActive(true);
            userService.cleanSave(user);

            BlockedUser blockedUser = blockedUserService.findById(user.getId());

            if (blockedUser == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            blockedUserService.delete(blockedUser);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Operation(summary = "Get all blocked users", description = "Return list of blocked users in specific format for each user: username/avatar, date blocked, admin's username/idAvatar, comment from admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created new post"),
            @ApiResponse(responseCode = "401", description = "Forbidden - user is not admin"),
            @ApiResponse(responseCode = "404", description = "Not found - no such a user"),
            @ApiResponse(responseCode = "400", description = "Bad request - user is already blocked and is in table")
    })
    @GetMapping("/blockedUsers")
    public ResponseEntity<List<ParsBlockedUser>> getBlockedUsers() {
        return blockedUserService.getAllBlockedUsers();
    }

    @PostMapping("/delReview")
    public ResponseEntity<Void> delReview(@RequestParam("idReview") Long idReview) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return blockedUserService.delAdminReview(idReview, authentication.getName());
    }

    @PostMapping("/delPost")
    public ResponseEntity<Void> delPost(@RequestParam("idPost") Long idPost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return blockedUserService.deletePost(authentication.getName(), idPost);
    }
}
