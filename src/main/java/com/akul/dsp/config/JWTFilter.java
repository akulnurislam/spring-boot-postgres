package com.akul.dsp.config;

import com.akul.dsp.dto.ErrorDTO;
import com.akul.dsp.util.Date;
import com.akul.dsp.util.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWT jwt;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final List<String> exclude = Arrays.asList(
            "/",
            "/health",
            "/request",
            "/login",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            String subject = jwt.getSubject(authorization);
            request.setAttribute("phoneNumber", subject);
        } catch (ExpiredJwtException ex) {
            log.warn("jwt expired", ex);
            forbiddenResponse(request, response);
            return;
        } catch (UnsupportedJwtException ex) {
            log.warn("jwt is not supported", ex);
            forbiddenResponse(request, response);
            return;
        } catch (MalformedJwtException ex) {
            log.warn("jwt is invalid", ex);
            forbiddenResponse(request, response);
            return;
        } catch (SignatureException ex) {
            log.warn("signature validation failed", ex);
            forbiddenResponse(request, response);
            return;
        } catch (IllegalArgumentException ex) {
            log.warn("token is null, empty or only whitespace", ex);
            forbiddenResponse(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return exclude.stream().anyMatch(p -> antPathMatcher.match(p, path));
    }

    private void forbiddenResponse(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws IOException {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .timestamp(Date.timestamp())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .path(request.getRequestURI())
                .build();
        ObjectMapper mapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(errorDTO));
        response.getWriter().flush();
    }
}
