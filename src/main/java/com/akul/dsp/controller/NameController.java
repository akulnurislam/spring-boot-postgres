package com.akul.dsp.controller;

import com.akul.dsp.dto.NameDTO;
import com.akul.dsp.dto.NameResponseDTO;
import com.akul.dsp.model.User;
import com.akul.dsp.service.UserService;
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
    private final UserService userService;

    @GetMapping
    @Operation(description = "API get name", security = @SecurityRequirement(name = "Authorization"))
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NameResponseDTO> getName(@RequestAttribute("phoneNumber") String phoneNumber) {
        User user = userService.findOneByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(
                NameResponseDTO.builder()
                        .name(user.getName())
                        .build());
    }

    @PutMapping
    @Operation(description = "API update name", security = @SecurityRequirement(name = "Authorization"))
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<NameResponseDTO> updateName(
            @RequestAttribute("phoneNumber") String phoneNumber,
            @RequestBody @Valid NameDTO nameDTO) {
        User user = userService.updateByPhoneNumber(phoneNumber, nameDTO);
        return ResponseEntity.ok(
                NameResponseDTO.builder()
                        .name(user.getName())
                        .build());
    }
}
