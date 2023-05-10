package com.akul.dsp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LoginDTO {
    @NotBlank()
    private String phoneNumber;

    @NotBlank
    private String password;
}
