package com.akul.dsp.exception;

import com.akul.dsp.dto.ErrorDTO;
import com.akul.dsp.util.Date;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import org.postgresql.util.PSQLException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(
            @NonNull MethodArgumentNotValidException ex, @NonNull WebRequest req) {
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
    public ResponseEntity<ErrorDTO> handleJWTException(@NonNull WebRequest req) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException(@NonNull NotFoundException ex, @NonNull WebRequest req) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.NOT_FOUND.value())
                .error(ex.getMessage())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDTO> handleUnauthorizedException(@NonNull WebRequest req) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataIntegrityViolationException(
            @NonNull DataIntegrityViolationException ex, @NonNull WebRequest req) {
        if (ex.getRootCause() != null && ex.getRootCause() instanceof PSQLException psqlEx)  {
            if (PSQLState.UNIQUE_VIOLATION.getState().equals(psqlEx.getSQLState())) {
                ErrorDTO errorDTO = ErrorDTO.builder()
                        .timestamp(Date.timestamp())
                        .status(HttpStatus.CONFLICT.value())
                        .error(HttpStatus.CONFLICT.getReasonPhrase())
                        .path(req.getDescription(false).substring(4))
                        .build();
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
            }
        }

        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(req.getDescription(false).substring(4))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
}
