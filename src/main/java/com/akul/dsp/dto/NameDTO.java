package com.akul.dsp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class NameDTO {
    @NotBlank
    @Size(max = 60, message = "size must be less than equal 60")
    private String name;
}
