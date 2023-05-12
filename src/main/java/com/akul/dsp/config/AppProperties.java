package com.akul.dsp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    private JWTProperties jwt;

    @Configuration
    @ConfigurationProperties(prefix = "jwt")
    @Data
    public static class JWTProperties {
        private int expiration;
        private String privateKey;
        private String publicKey;
    }
}
