package com.akul.dsp.controller;

import com.akul.dsp.dto.LoginDTO;
import com.akul.dsp.dto.LoginResponseDTO;
import com.akul.dsp.service.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
@Tag(name = "Login")
public class LoginController {
    private final LoginService loginService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return ResponseEntity.ok(
                LoginResponseDTO.builder()
                        .token(loginService.generateToken(loginDTO))
                        .build());
    }
}
