package com.akul.dsp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class LoginResponseDTO {
    private String token;
}
