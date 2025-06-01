package com.example.SABS.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
//service to manipulate the JWT token
public class JwtService {

    private static final String secretKey = "35e648aa2a1204ef61e843a7dc758ccb5825b3734400bded248576c39470d67aee15427bbd064cdc9c0a69fa3c7bf237b4ae702fa02f3b774220a99c532a1c681ee67b4af0d67ca101553799c9da73fb032ee62f8a99f1b6de5dd4274399792ba2bede82a7b674282aafe9fc428d3b7b3068ed2bad6d9ec120fc945dfdfe12d7755df921c0d6c79284a7e5088ab39d235fb6a4a67c597f6c2801e9221025117adebec97828f77eed9e0a0468144ac71f26ff53c37a56b2606294fa1ecf48e86b93662136756280bb0a495b760afa778c5d2605f4b3b161f1e58f0df3f4734df6edf89e6c8ee32fc2e94a687bed7e56a5ab53940f07bc5bfede2289ecd73c7f27";

    //method to extract the userName from the token
    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }

    //method to extract a single claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //method to extract all claims from the token
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSiginingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSiginingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //method that will help us generate a token, map that contains extra claims
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSiginingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //method to generate a token from the user details
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    //method that can validate a token
    //we want to validate if this token belongs to this user details
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //method to check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //method to extract the expiration date of the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



}
