package com.joe.lib.config;

import com.joe.lib.shared.AppConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    //    private static final String SECRET_KEY = "645367566B5970337336763979244226452948404D6351665468576D5A713474";
    private final GenerateSecretKeyConfig generateSecretKeyConfig;

    @Value("${jwt.expiration-time}")
    private long JWT_EXPIRATION_TIME;
    @Value("${jwt.refresh-token-expiration-time}")
    private long JWT_REFRESH_EXPIRATION_TIME;

    public String generateToken(String email, Map<String, Object> extraClaim) {
        return buildToken(extraClaim, email, JWT_EXPIRATION_TIME);
    }

    public String generateRefreshToken(String email, Map<String, Object> extraClaim) {
        return buildToken(extraClaim, email, JWT_REFRESH_EXPIRATION_TIME);
    }

    public String buildToken(Map<String, Object> extraClaims, String email, long expiryTime) {

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime)) // 3 DAYS
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        final Integer roleId = extractRoleType(token);
        final int[] userDetailsRoleId = new int[1];
        userDetails.getAuthorities().stream().findFirst().ifPresent(grantedAuthority -> userDetailsRoleId[0] = RoleType.valueOf(grantedAuthority.getAuthority()).roleId);

        return (username.equals(userDetails.getUsername())) && (roleId == userDetailsRoleId[0]) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Integer extractRoleType(String token) {
        return (Integer) extractAllClaims(token).get(AppConstants.roleType);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public Claims extractAllClaims(String token) {

        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new JwtException("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new JwtException("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new JwtException("JWT claims string is empty: " + e.getMessage());
        }
    }


    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(generateSecretKeyConfig.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }


}