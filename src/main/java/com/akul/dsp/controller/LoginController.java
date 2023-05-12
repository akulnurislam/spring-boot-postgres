package com.akul.dsp.controller;

import com.akul.dsp.dto.LoginDTO;
import com.akul.dsp.dto.LoginResponseDTO;
import com.akul.dsp.util.JWT;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {
    private final JWT jwt;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        System.out.println(loginDTO);
        return ResponseEntity.ok(
                LoginResponseDTO.builder()
                        .token(jwt.generate(loginDTO.getPhoneNumber()))
                        .build()
        );
    }
}
