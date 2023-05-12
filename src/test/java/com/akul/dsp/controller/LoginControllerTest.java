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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

        when(loginService.generateToken(any(LoginDTO.class))).thenReturn(token);

        String json = """
                {
                  "phoneNumber": "081288885555",
                  "password": "abcdE1"
                }
                """;
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void loginNotFoundTest() throws Exception {
        when(loginService.generateToken(any(LoginDTO.class))).thenThrow(new NotFoundException("notfound"));

        String json = """
                {
                  "phoneNumber": "081288885555",
                  "password": "abcdE1"
                }
                """;
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }
}
