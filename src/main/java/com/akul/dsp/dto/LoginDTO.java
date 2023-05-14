package com.akul.dsp.dto;

import com.akul.dsp.validator.CapitalAndNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class LoginDTO {
    @NotBlank
    @Size(min = 10, max = 13)
    @Pattern(regexp = "^08\\d+", message = "only number and must be started with 08")
    private String phoneNumber;

    @NotBlank
    private String password;
}
