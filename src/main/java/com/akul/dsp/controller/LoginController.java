package com.akul.dsp.controller;

import com.akul.dsp.dto.LoginDTO;
import com.akul.dsp.util.JWT;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {
    private final JWT jwt;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        System.out.println(loginDTO);
        return ResponseEntity.ok(jwt.generate(loginDTO.getPhoneNumber()));
    }
}
