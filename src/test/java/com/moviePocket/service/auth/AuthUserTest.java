package com.moviePocket.service.auth;


import com.moviePocket.db.entities.user.User;
import com.moviePocket.exception.UnauthorizedException;
import com.moviePocket.service.impl.auth.AuthUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthUserTest {

    private final AuthUser authUser = new AuthUser();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetAuthenticatedUser_returnsUser() {
        // given
        User user = new User();
        user.setUsername("test_user");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // when
        User result = authUser.getAuthenticatedUser();

        // then
        assertNotNull(result);
        assertEquals("test_user", result.getUsername());
    }


    @Test
    void testGetAuthenticatedUser_whenAuthenticationNull_shouldThrow() {
        // given
        SecurityContextHolder.clearContext();

        // then
        assertThrows(UnauthorizedException.class, authUser::getAuthenticatedUser);
    }

    @Test
    void testGetAuthenticatedUser_whenNotAuthenticated_shouldThrow() {
        // given
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // then
        assertThrows(UnauthorizedException.class, authUser::getAuthenticatedUser);
    }

    @Test
    void testGetAuthenticatedUser_whenPrincipalIsString_shouldThrow() {
        // given
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // then
        assertThrows(UnauthorizedException.class, authUser::getAuthenticatedUser);
    }

}
