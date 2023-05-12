package com.akul.dsp.exception;

import com.akul.dsp.dto.ErrorDTO;
import com.akul.dsp.util.Date;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest req) {
        List<String> fields = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String fieldMessage = String.format("%s - %s", error.getField(), error.getDefaultMessage());
            fields.add(fieldMessage);
        }
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .fields(fields)
                .build();
        return ResponseEntity.badRequest().body(errorDTO);
    }

    @ExceptionHandler({
            ExpiredJwtException.class,
            UnsupportedJwtException.class,
            MalformedJwtException.class,
            SignatureException.class,
    })
    public ResponseEntity<ErrorDTO> handleJWTException(WebRequest req) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(NotFoundException ex, WebRequest req) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.NOT_FOUND.value())
                .error(ex.getMessage())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDTO> handleUnauthorizedException(WebRequest req) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }
}
