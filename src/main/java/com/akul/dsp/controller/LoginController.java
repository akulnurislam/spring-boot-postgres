package com.akul.dsp.controller;

import com.akul.dsp.dto.LoginDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        System.out.println(loginDTO);
        return ResponseEntity.ok("Success");
    }
}
