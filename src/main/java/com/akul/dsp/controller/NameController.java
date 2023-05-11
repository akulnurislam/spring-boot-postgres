package com.akul.dsp.controller;

import com.akul.dsp.dto.NameDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping
    public ResponseEntity<String> getName() {
        return ResponseEntity.ok("imname");
    }

    @PutMapping
    public ResponseEntity<Void> updateName(@RequestBody @Valid NameDTO nameDTO) {
        System.out.println(nameDTO);
        return ResponseEntity.ok().build();
    }
}
