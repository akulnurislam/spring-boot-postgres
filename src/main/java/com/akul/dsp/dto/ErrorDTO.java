package com.akul.dsp.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ErrorDTO {
    private String timestamp;
    private int status;
    private String error;
    private String path;
    private List<String> fields;
}
