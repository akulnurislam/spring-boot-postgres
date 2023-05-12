package com.akul.dsp.controller;

import com.akul.dsp.dto.RequestDTO;
import com.akul.dsp.model.User;
import com.akul.dsp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/request")
public class RequestController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> request(@RequestBody @Valid RequestDTO requestDTO) {
        userService.create(
                User.builder()
                        .phoneNumber(requestDTO.getPhoneNumber())
                        .name(requestDTO.getName())
                        .password(requestDTO.getPassword())
                        .build());
        return ResponseEntity.created(URI.create("/request")).build();
    }
}
