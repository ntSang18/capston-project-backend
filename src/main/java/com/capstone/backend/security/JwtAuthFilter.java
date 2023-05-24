package com.capstone.backend.security;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.capstone.backend.dto.exception.ExceptionResponse;
import com.capstone.backend.service.serviceImpl.UserDetailsServiceImpl;
import com.capstone.backend.util.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtils jwtService;

  private final UserDetailsServiceImpl userDetailService;

  private final MessageSource messageSource;

  private final Jackson2ObjectMapperBuilder mapperBuilder;

  @Value("${application.server.time-zone}")
  private String timeZone;

  @Value("${application.security.access-token-secret}")
  private String accessTokenSecret;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String authHeader = request.getHeader("Authorization");
      String token;
      String email;

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }

      token = authHeader.substring(7);
      email = jwtService.extractUsername(token, accessTokenSecret);

      if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailService.loadUserByUsername(email);

        if (jwtService.validateToken(token, accessTokenSecret, userDetails)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }

      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException e) {
      ZoneId zoneId = ZoneId.of(timeZone);
      ExceptionResponse exceptionResponse = new ExceptionResponse(
          messageSource.getMessage("error.unauthenticated", null, Locale.getDefault()),
          401,
          ZonedDateTime.now(zoneId));
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write(convertObjectToJson(exceptionResponse));
    }
  }

  private String convertObjectToJson(Object object) throws JsonProcessingException {
    if (object == null) {
      return null;
    }
    ObjectMapper mapper = mapperBuilder.build();
    return mapper.writeValueAsString(object);
  }

}
