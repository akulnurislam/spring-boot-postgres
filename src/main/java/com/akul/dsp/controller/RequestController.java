package com.akul.dsp.controller;

import com.akul.dsp.dto.RequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/request")
public class RequestController {

    @PostMapping
    public ResponseEntity<Void> request(@RequestBody @Valid RequestDTO requestDTO) {
        System.out.println(requestDTO);
        return ResponseEntity.created(URI.create("/request")).build();
    }
}
