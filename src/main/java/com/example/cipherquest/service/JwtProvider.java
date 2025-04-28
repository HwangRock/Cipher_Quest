package com.example.cipherquest.service;

import com.example.cipherquest.model.Role;
import com.example.cipherquest.model.Tier;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final long ACCESS_TOKEN_EXPIRATION = 1000*60*30; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 14 * 86400000; // 14일
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String createAccessToken(String userid, Role role, Tier tier, String username){
        return Jwts.builder()
                .setClaims(Map.of(
                        "userId", userid,
                        "Role", role,
                        "Tier", tier,
                        "userName", username
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken() {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
