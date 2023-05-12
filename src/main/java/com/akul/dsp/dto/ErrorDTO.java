package com.akul.dsp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@ToString
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {
    private String timestamp;
    private int status;
    private String error;
    private String path;
    private List<String> fields;
}
