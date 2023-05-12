package com.akul.dsp.controller;

import com.akul.dsp.dto.NameDTO;
import com.akul.dsp.exception.NotFoundException;
import com.akul.dsp.model.User;
import com.akul.dsp.service.UserService;
import com.akul.dsp.util.JWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = NameController.class,
        excludeAutoConfiguration = {
                DataSourceAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class,
                SecurityAutoConfiguration.class,
        }
)
@ActiveProfiles("test")
public class NameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWT jwt;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void tearDown() {
        Mockito.reset(jwt);
        Mockito.reset(userService);
    }

    @Test
    public void getNameTest() throws Exception {
        String token = "im-token";
        String phoneNumber = "081288885555";
        String name = "im-name";

        User user = new User();
        user.setName(name);

        when(jwt.getSubject(token)).thenReturn(phoneNumber);
        when(userService.findOneByPhoneNumber(phoneNumber)).thenReturn(user);

        mockMvc.perform(get("/name")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void getNameNotFoundTest() throws Exception {
        String token = "im-token";
        String phoneNumber = "081288885555";

        when(jwt.getSubject(token)).thenReturn(phoneNumber);
        when(userService.findOneByPhoneNumber(phoneNumber)).thenThrow(new NotFoundException("notfound"));

        mockMvc.perform(get("/name")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateNameTest() throws Exception {
        String token = "im-token";
        String phoneNumber = "081288885555";
        String updatedName = "im-updated-name";

        User user = new User();
        user.setName(updatedName);

        when(jwt.getSubject(token)).thenReturn(phoneNumber);
        when(userService.updateByPhoneNumber(eq(phoneNumber), any(NameDTO.class))).thenReturn(user);

        String json = format("""
                {
                  "name": "%s"
                }
                """, updatedName);
        mockMvc.perform(put("/name")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedName));
    }

    @Test
    public void updateNameNotFoundTest() throws Exception {
        String token = "im-token";
        String phoneNumber = "081288885555";

        when(jwt.getSubject(token)).thenReturn(phoneNumber);
        when(userService.updateByPhoneNumber(eq(phoneNumber), any(NameDTO.class)))
                .thenThrow(new NotFoundException("notfound"));

        String json = """
                {
                  "name": "im-new-name"
                }
                """;
        mockMvc.perform(put("/name")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }
}
