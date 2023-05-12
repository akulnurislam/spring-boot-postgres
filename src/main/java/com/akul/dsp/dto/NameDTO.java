package com.akul.dsp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class NameDTO {
    @NotBlank
    @Size(max = 60, message = "size must be less than equal 60")
    private String name;
}
