package com.capstone.backend.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

  public String extractUsername(String token, String secret_key) {
    return extractClaims(token, secret_key, Claims::getSubject);
  }

  public Date extractExpiration(String token, String secret_key) {
    return extractClaims(token, secret_key, Claims::getExpiration);
  }

  public String generateToken(String username, long exp, String secret_key) {
    Map<String, Object> claims = new HashMap<>();
    return CreateToken(claims, username, exp, secret_key);
  }

  public <T> T extractClaims(String token, String secret_key, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token, secret_key);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token, String secret_key) {
    return Jwts
        .parser()
        .setSigningKey(secret_key)
        .parseClaimsJws(token)
        .getBody();
  }

  public Boolean isTokenExpired(String token, String secret_key) {
    return extractExpiration(token, secret_key).before(new Date());
  }

  public Boolean validateToken(String token, String secret_key, UserDetails userDetails) {
    final String username = extractUsername(token, secret_key);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, secret_key));
  }

  private String CreateToken(Map<String, Object> claims, String username, Long exp, String secret_key) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + exp))
        .signWith(SignatureAlgorithm.HS256, secret_key).compact();
  }

}
