package com.linreelle.saphir.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;


    private String buildToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails,
            long expiration){
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(),  SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (SignatureException e){
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e){
            throw new JwtException("Invalid JWT token");
        }
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return  claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        // Find the authority that starts with "ROLE_" and strip the prefix
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.substring(5)) // remove "ROLE_"
                .findFirst()
                .orElse("UNKNOWN");

        claims.put("role", role);

        return generateToken(claims, userDetails);

    }
    public String generateToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails){
        long jwtExpiration = 86400000;
        return buildToken(extractClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails){
        long refreshExpiration = 604800000;
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }


    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username = extractUsername(token);
        log.info("isTokenValid: username = {}", username);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private  boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private  Claims extractAllClaims(String token){
        Claims claims = Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        log.info("Token claims: {}", claims);
        return claims;

    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


