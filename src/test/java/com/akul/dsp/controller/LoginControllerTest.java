package com.akul.dsp.controller;

import com.akul.dsp.dto.LoginDTO;
import com.akul.dsp.exception.NotFoundException;
import com.akul.dsp.service.LoginService;
import com.akul.dsp.util.JWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = LoginController.class,
        excludeAutoConfiguration = {
                DataSourceAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class,
                SecurityAutoConfiguration.class,
        }
)
@ActiveProfiles("test")
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWT jwt;

    @MockBean
    private LoginService loginService;

    @Test
    public void loginTest() throws Exception {
        String token = "im-token";
        String phoneNumber = "081288885555";
        String password = "abcdE1";

        LoginDTO dto = new LoginDTO();
        dto.setPhoneNumber(phoneNumber);
        dto.setPassword(password);

        when(loginService.generateToken(dto)).thenReturn(token);

        String json = format("""
                {
                  "phoneNumber": "%s",
                  "password": "%s"
                }
                """, phoneNumber, password);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));

        verify(loginService, times(1)).generateToken(dto);
    }

    @Test
    public void loginNotFoundTest() throws Exception {
        String phoneNumber = "081288885555";
        String password = "abcdE1";

        LoginDTO dto = new LoginDTO();
        dto.setPhoneNumber(phoneNumber);
        dto.setPassword(password);

        when(loginService.generateToken(dto)).thenThrow(new NotFoundException("notfound"));

        String json = format("""
                {
                  "phoneNumber": "%s",
                  "password": "%s"
                }
                """, phoneNumber, password);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        verify(loginService, times(1)).generateToken(dto);
    }
}
