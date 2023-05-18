package com.akul.dsp.controller;

import com.akul.dsp.dto.RequestDTO;
import com.akul.dsp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/request")
@Tag(name = "Request")
public class RequestController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> request(@RequestBody @Valid RequestDTO requestDTO) {
        userService.create(requestDTO);
        return ResponseEntity.created(URI.create("/request")).build();
    }
}
