package com.visitly.myproject.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {

    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class.getName());

    @Value("${jwt.secret:mySecretKeyForJwtTokenGenerationAndValidationPurpose1234567890}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;

    public String generateToken(String email) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            logger.severe("Error generating JWT token: " + e.getMessage());
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (JwtException e) {
            logger.severe("Error extracting email from JWT: " + e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            logger.fine("JWT token is valid");
            return true;
        } catch (MalformedJwtException ex) {
            logger.severe("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.severe("Expired JWT token: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.severe("Unsupported JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.severe("JWT claims string is empty: " + ex.getMessage());
        } catch (Exception ex) {
            logger.severe("JWT token validation error: " + ex.getMessage());
        }
        return false;
    }

    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
