package com.example.cipherquest.service;

import com.example.cipherquest.model.Role;
import com.example.cipherquest.model.Tier;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static final long ACCESS_TOKEN_EXPIRATION = 1000*60*60; // 1시간
    private Key key;

    @PostConstruct
    public void init() {
        this.key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
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
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
