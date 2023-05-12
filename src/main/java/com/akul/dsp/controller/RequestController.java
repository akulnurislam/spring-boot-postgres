package com.akul.dsp.controller;

import com.akul.dsp.dto.RequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/request")
public class RequestController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> request(@RequestBody @Valid RequestDTO requestDTO) {
        System.out.println(requestDTO);
        return ResponseEntity.created(URI.create("/request")).build();
    }
}
