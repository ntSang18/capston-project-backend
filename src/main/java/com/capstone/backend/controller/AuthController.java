package com.capstone.backend.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.backend.dto.AuthInfo;
import com.capstone.backend.dto.ChangePasswordRequest;
import com.capstone.backend.dto.LoginRequest;
import com.capstone.backend.dto.MailInfoRequest;
import com.capstone.backend.dto.MessageResponse;
import com.capstone.backend.dto.Oauth2Request;
import com.capstone.backend.dto.RegisterRequest;
import com.capstone.backend.dto.ResetRequest;
import com.capstone.backend.dto.TokenResponse;
import com.capstone.backend.exception.AccountDisableException;
import com.capstone.backend.exception.AccountLockedException;
import com.capstone.backend.exception.ConfirmedException;
import com.capstone.backend.exception.EmailTakenException;
import com.capstone.backend.exception.ExpiredTokenException;
import com.capstone.backend.exception.InvalidCredentialsException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.exception.UnauthenticatedException;
import com.capstone.backend.model.UserDetailsImpl;
import com.capstone.backend.service.iservice.IAuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {

  private final IAuthService authService;

  @PostMapping(value = "/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest req)
      throws EmailTakenException {
    authService.register(req);
    return new ResponseEntity<>(
        new MessageResponse("Check your email for activate"),
        HttpStatus.OK);
  }

  @GetMapping(value = "/confirm/{token}")
  public ResponseEntity<?> confirm(@PathVariable String token)
      throws ResourceNotFoundException,
      ExpiredTokenException,
      ConfirmedException {
    authService.confirm(token);
    return new ResponseEntity<>(
        new MessageResponse("Register successfully!"),
        HttpStatus.OK);
  }

  @PostMapping(value = "/reconfirm")
  public ResponseEntity<?> reconfirm(@RequestBody MailInfoRequest req)
      throws ResourceNotFoundException,
      ConfirmedException {
    authService.reconfirm(req.email());
    return new ResponseEntity<>(
        new MessageResponse("Check your email for active"),
        HttpStatus.OK);
  }

  @PostMapping(value = "/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletResponse response)
      throws AccountDisableException,
      InvalidCredentialsException,
      AccountLockedException {
    AuthInfo res = authService.login(req);
    Cookie cookie = new Cookie("refresh_token", res.refreshToken());
    cookie.setMaxAge(3600);
    response.addCookie(cookie);

    return new ResponseEntity<>(new TokenResponse(res.token()), HttpStatus.OK);
  }

  @PostMapping(value = "/google-oauth2")
  public ResponseEntity<?> googleOauth2(@RequestBody Oauth2Request req, HttpServletResponse resp)
      throws InvalidCredentialsException,
      EmailTakenException,
      GeneralSecurityException,
      IOException {
    AuthInfo res = authService.googleOAuth2Login(req.idToken());

    Cookie cookie = new Cookie("refresh_token", res.refreshToken());
    cookie.setMaxAge(3600);

    return new ResponseEntity<>(new TokenResponse(res.token()), HttpStatus.OK);
  }

  @PostMapping(value = "/refresh")
  public ResponseEntity<?> refresh(@CookieValue("refresh_token") String refresh_token)
      throws UnauthenticatedException,
      ResourceNotFoundException {
    AuthInfo res = authService.refreshToken(refresh_token);
    return new ResponseEntity<>(
        new TokenResponse(res.token()),
        HttpStatus.OK);
  }

  @PostMapping(value = "/logout")
  public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
    authService.logout(request, response);
    return new ResponseEntity<>(
        new MessageResponse("Logout success!"),
        HttpStatus.OK);
  }

  @PostMapping(value = "/forgot")
  public ResponseEntity<?> forgot(@RequestBody MailInfoRequest req)
      throws ResourceNotFoundException,
      AccountDisableException {
    authService.forgot(req.email());
    return new ResponseEntity<>(
        new MessageResponse("Check your email to reset password"),
        HttpStatus.OK);
  }

  @PostMapping(value = "/reset")
  public ResponseEntity<?> reset(@RequestBody ResetRequest req)
      throws ResourceNotFoundException,
      InvalidCredentialsException {
    authService.reset(req.token(), req.password(), req.confirmPassword());
    return new ResponseEntity<>(
        new MessageResponse("Password has been changed"),
        HttpStatus.OK);
  }

  @PostMapping(value = "/change")
  public ResponseEntity<?> change(
      @RequestBody ChangePasswordRequest request,
      @AuthenticationPrincipal UserDetailsImpl user)
      throws ResourceNotFoundException, InvalidCredentialsException {
    authService.change(user.getUsername(), request);
    return new ResponseEntity<>(
        new MessageResponse("Password has been change"),
        HttpStatus.OK);
  }
}
