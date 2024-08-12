package com.o11ezha.controlpanel.security.jwt;

import com.o11ezha.controlpanel.security.userdetails.ControlPanelUserDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${token.secret}")
    private String jwtSecret;

    @Value("${token.timeToLive}")
    private int jwtExpirationTime;

    public String generateJwtTokenForUser(Authentication authentication){
        ControlPanelUserDetails userPrincipal = (ControlPanelUserDetails) authentication.getPrincipal();

        return Jwts.builder().setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getExpirationDate())
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try {
            Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e){
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    private Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtExpirationTime);
    }

    private Key getSigninKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
