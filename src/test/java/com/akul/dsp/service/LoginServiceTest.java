package com.akul.dsp.service;

import com.akul.dsp.dto.LoginDTO;
import com.akul.dsp.exception.UnauthorizedException;
import com.akul.dsp.model.User;
import com.akul.dsp.service.impl.LoginServiceImpl;
import com.akul.dsp.util.JWT;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    private final UserService userService = Mockito.mock(UserService.class);
    private final JWT jwt = Mockito.mock(JWT.class);
    private final LoginService loginService = new LoginServiceImpl(userService, jwt);

    @Test
    public void generateTokenTest() {
        String phoneNumber = "081288885555";
        String password = "im-password";
        String passwordHashed = "$2a$10$35S7ZzC62E95R0oi8kGkt.1MUOFMaLWCVX8RxwHk8t0KQsMh3pcqq";
        String salt = "im-salt";
        String token = "eyJzdWIiOiIwODEyODg4ODU1NTUifQ";

        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(passwordHashed);
        user.setSalt(salt);

        when(userService.findOneByPhoneNumber(phoneNumber)).thenReturn(user);
        when(jwt.generate(phoneNumber)).thenReturn(token);

        LoginDTO dto = new LoginDTO();
        dto.setPhoneNumber(phoneNumber);
        dto.setPassword(password);
        String generatedToken = loginService.generateToken(dto);

        verify(userService, times(1)).findOneByPhoneNumber(phoneNumber);
        verify(jwt, times(1)).generate(phoneNumber);
        assertEquals(token, generatedToken);
    }

    @Test
    public void generateTokenUnauthorizedTest() {
        String phoneNumber = "081288885555";
        String password = "im-wrong-password";
        String hashedPassword = "$2a$10$35S7ZzC62E95R0oi8kGkt.1MUOFMaLWCVX8RxwHk8t0KQsMh3pcqq";
        String salt = "im-salt";

        User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setPassword(hashedPassword);
        user.setSalt(salt);

        when(userService.findOneByPhoneNumber(phoneNumber)).thenReturn(user);

        assertThrows(UnauthorizedException.class, () -> {
            LoginDTO dto = new LoginDTO();
            dto.setPhoneNumber(phoneNumber);
            dto.setPassword(password);
            loginService.generateToken(dto);
        });

        verify(userService, times(1)).findOneByPhoneNumber(phoneNumber);
    }
}
