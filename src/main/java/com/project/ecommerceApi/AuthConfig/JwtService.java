package com.project.ecommerceApi.AuthConfig;

import com.project.ecommerceApi.Entity.Token;
import com.project.ecommerceApi.Repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;
    @Value("${api.secretkey}")
    private String secretKey;
    @Value("${expire.jw-token}")
    private int jwtTokenExpire;
    @Value("${expire.refresh-token}")
    private int refreshTokenExpire;

    /*Extract the username from token*/
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /*Extract a claim from token*/
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /*Extract all the claims from token*/
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /*Generate an access token*/
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, jwtTokenExpire);
    }

    /*Generate a refesh token*/
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, refreshTokenExpire);
    }

    /*Method to generate the token*/
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, int expireDuration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireDuration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    /*Check if token is valid*/
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        Token jwt = tokenRepository.findByJwtToken(token).orElse(null);
        if (jwt != null) {
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && !jwt.isLogout();
        }
        return false;
    }

    /*Check if the token has expired*/
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /*Exctract the expiration date*/
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*Decode the secret key and get the signIn key*/
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
