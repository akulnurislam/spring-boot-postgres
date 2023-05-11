package com.akul.dsp.dto;

import com.akul.dsp.validator.CapitalAndNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LoginDTO {
    @NotBlank
    @Size(min = 10, max = 13)
    @Pattern(regexp = "^08\\d+", message = "only number and must be started with 08")
    private String phoneNumber;

    @NotBlank
    @Size(min = 6, max = 16)
    @CapitalAndNumber(message = "must be containing at least 1 capital letter and 1 number")
    private String password;
}
