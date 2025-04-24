package com.thesys.titan.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j // 롬복을 이용하여 로깅을 위한 Logger 선언
@Component
public class JwtTokenProvider {

    // private SecretKey secretKey;

    // public JwtTokenProvider(@Value("${jwt.secret}") String secret) {

    // this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
    // Jwts.SIG.HS256.key().build().getAlgorithm());
    // }

    // public String getUsername(String token) {

    // return
    // Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username",
    // String.class);
    // }

    // public String getRole(String token) {

    // return
    // Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role",
    // String.class);
    // }

    // public Boolean isExpired(String token) {

    // return
    // Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
    // .before(new Date());
    // }

    // public String createJwt(String username, String role, Long expiredMs) {

    // return Jwts.builder()
    // .claim("username", username)
    // .claim("role", role)
    // .issuedAt(new Date(System.currentTimeMillis()))
    // .expiration(new Date(System.currentTimeMillis() + expiredMs))
    // .signWith(secretKey)
    // .compact();
    // }
}