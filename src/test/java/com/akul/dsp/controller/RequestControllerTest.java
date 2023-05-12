package com.akul.dsp.controller;

import com.akul.dsp.dto.RequestDTO;
import com.akul.dsp.service.UserService;
import com.akul.dsp.util.JWT;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = RequestController.class,
        excludeAutoConfiguration = {
                DataSourceAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class,
                SecurityAutoConfiguration.class,
        }
)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWT jwt;

    @MockBean
    private UserService userService;

    @Test
    public void requestTest() throws Exception {
        doNothing().when(userService).create(any(RequestDTO.class));

        String json = """
                {
                  "phoneNumber": "081288885555",
                  "name": "im-name",
                  "password": "abcdE1"
                }
                """;
        mockMvc.perform(post("/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(userService, times(1)).create(any(RequestDTO.class));
    }

    @Test
    public void requestConflictTest() throws Exception {
        PSQLException psqlEx = new PSQLException("duplicate", PSQLState.UNIQUE_VIOLATION);
        DataIntegrityViolationException divEx = new DataIntegrityViolationException("duplicate", psqlEx);

        doThrow(divEx).when(userService).create(any(RequestDTO.class));

        String json = """
                {
                  "phoneNumber": "081288885555",
                  "name": "im-name",
                  "password": "abcdE1"
                }
                """;
        mockMvc.perform(post("/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    public void requestPhoneNumberTest() throws Exception {
        String sizeMessage = "phoneNumber - size must be between 10 and 13";
        String patternMessage = "phoneNumber - only number and must be started with 08";

        String json = """
                {
                  "phoneNumber": "1234a",
                  "name": "im-name",
                  "password": "abcdE1"
                }
                """;
        mockMvc.perform(post("/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields", hasItems(sizeMessage)))
                .andExpect(jsonPath("$.fields", hasItems(patternMessage)));
    }

    @Test
    public void requestNameTest() throws Exception {
        String sizeMessage = "name - size must be less than equal 60";

        String json = """
                {
                  "phoneNumber": "081288885555",
                  "name": "im-loooooooooooooooooooooooooooooooooooooooooooooooooong-name",
                  "password": "abcdE1"
                }
                """;
        mockMvc.perform(post("/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields", hasItems(sizeMessage)));
    }

    @Test
    public void requestPasswordTest() throws Exception {
        String sizeMessage = "password - size must be between 6 and 16";
        String patternMessage = "password - must be containing at least 1 capital letter and 1 number";

        String json = """
                {
                  "phoneNumber": "081288885555",
                  "name": "im-name",
                  "password": "-"
                }
                """;
        mockMvc.perform(post("/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields", hasItems(sizeMessage)))
                .andExpect(jsonPath("$.fields", hasItems(patternMessage)));
    }
}
