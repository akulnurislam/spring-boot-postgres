package com.akul.dsp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {

    private RSAProperties rsa;
    private JWTProperties jwt;

    @Configuration
    @ConfigurationProperties(prefix = "rsa")
    @Data
    public static class RSAProperties {
        private String privateKey;
        private String publicKey;
    }

    @Configuration
    @ConfigurationProperties(prefix = "jwt")
    @Data
    public static class JWTProperties {
        private int expiration;
    }
}
