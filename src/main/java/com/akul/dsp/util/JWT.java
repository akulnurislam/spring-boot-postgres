package com.akul.dsp.util;

import com.akul.dsp.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JWT {

    private final AppProperties appProperties;

    private KeyPair keyPair;

    @PostConstruct
    public void init() {
        try {
            PKCS8EncodedKeySpec privateKeySpec =
                    new PKCS8EncodedKeySpec(encodedBytes(appProperties.getJwt().getPrivateKey()));
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedBytes(appProperties.getJwt().getPublicKey()));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            keyPair = new KeyPair(publicKey, privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException("error generate key pair", ex);
        }
    }

    private byte[] encodedBytes(String fileName) {
        try {
            String path = Paths.get(System.getProperty("user.dir"), fileName).toString();
            byte[] keyBytes = Files.readAllBytes(new File(path).toPath());

            String key = new String(keyBytes);
            key = key.replace("-----BEGIN PRIVATE KEY-----", "");
            key = key.replace("-----END PRIVATE KEY-----", "");
            key = key.replace("-----BEGIN PUBLIC KEY-----", "");
            key = key.replace("-----END PUBLIC KEY-----", "");
            key = key.replaceAll("\\s+", "");

            return Base64.getDecoder().decode(key);
        } catch (IOException ex) {
            throw new RuntimeException("error encode byte key", ex);
        }
    }

    public String generate(String subject) {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, appProperties.getJwt().getExpiration());
        Date exp = calendar.getTime();

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String getSubject(String token) {
        if (Strings.isEmpty(token)) {
            throw new IllegalArgumentException("token empty");
        }

        String t = token.replaceAll("^[Bb]earer ", "");
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(t);
        return claims.getBody().getSubject();
    }
}
