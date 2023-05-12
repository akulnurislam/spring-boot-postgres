package com.akul.dsp.controller;

import com.akul.dsp.dto.NameDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/name")
@SecurityScheme(name = "Authorization", type = SecuritySchemeType.HTTP, scheme = "bearer")
public class NameController {

    @GetMapping
    @Operation(description = "API get name", security = @SecurityRequirement(name = "Authorization"))
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getName(
            @RequestAttribute("phoneNumber") String phoneNumber
    ) {
        return ResponseEntity.ok(phoneNumber);
    }

    @PutMapping
    @Operation(description = "API update name", security = @SecurityRequirement(name = "Authorization"))
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updateName(@RequestBody @Valid NameDTO nameDTO) {
        System.out.println(nameDTO);
        return ResponseEntity.ok().build();
    }
}
