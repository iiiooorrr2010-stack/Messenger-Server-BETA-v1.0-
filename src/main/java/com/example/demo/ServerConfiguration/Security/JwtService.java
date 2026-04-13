package com.example.demo.ServerConfiguration.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.security.Key;
import java.util.*;
@Slf4j
@Service
public class JwtService {
    @Value("${jwt.secret:8DF9fKmTxqBDcnCLa5n1EKgoGyKqj5yTgxO5sdOUlIK}")
    private String jwtSecret;
    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(Map<String, Object> claims, String id) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        /*
        1 - получаем клайм
        2 - получаем пользователя, который  верифицируется
        3, 4 - устанавливаем срок действия токена
        5 - получаем токен(ключ подписи)
         */
    }

    public String generateToken(String id, String nickName) {
        log.info("generate token id: {}, nickname: {}", id, nickName);
        Map<String, Object> claims = new HashMap<>();
        claims.put("nick", nickName);
        return createToken(claims, id);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractNickName(String token) {
    return extractClaim(token, claims -> claims.get("nickname", String.class));

    }

    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Boolean isTokenValid(String token, String id) {
        final String exctractedUserId = extractUserId(id);
        return (exctractedUserId.equals(id) && !isTokenExpired(token));
    }
}
