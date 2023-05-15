package com.capstone.backend.service.serviceImpl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.backend.constant.ErrorMessage;
import com.capstone.backend.constant.JwtConstants;
import com.capstone.backend.constant.Roles;
import com.capstone.backend.dto.AuthInfo;
import com.capstone.backend.dto.LoginRequest;
import com.capstone.backend.dto.RegisterRequest;
import com.capstone.backend.exception.ConfirmedException;
import com.capstone.backend.exception.EmailTakenException;
import com.capstone.backend.exception.ExpiredTokenException;
import com.capstone.backend.exception.InvalidCredentialsException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.exception.UnauthenticatedException;
import com.capstone.backend.exception.UnconfirmedException;
import com.capstone.backend.model.Token;
import com.capstone.backend.model.User;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.service.iservice.IAuthService;
import com.capstone.backend.service.iservice.IEmailService;
import com.capstone.backend.service.iservice.ITokenService;
import com.capstone.backend.util.JwtUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtUtils jwtService;

  private final ITokenService tokenService;

  private final IEmailService emailService;

  private final GoogleIdTokenVerifier googleVerifier;

  @Value("${application.security.access-token-secret}")
  private String accessTokenSecret;

  @Value("${application.security.refresh-token-secret}")
  private String refreshTokenSecret;

  @Value("${application.frontend.default-url}")
  private String frontendUrl;

  @Value("${application.backend.default-url}")
  private String backendUrl;

  @Override
  public void register(RegisterRequest req) throws EmailTakenException {
    if (userRepository.findByEmail(req.email()).isPresent()) {
      throw new EmailTakenException("Email taken");
    }

    String token = signUp(
        new User(
            req.email(),
            req.password(),
            req.phoneNumber(),
            req.username(),
            Roles.ROLE_USER));

    String apiEndPoint = "/api/auth/confirm/";
    String url = backendUrl + apiEndPoint + token;
    emailService.sendConfirm(req.email(), req.username(), url);
  }

  @Override
  public void confirm(String token)
      throws ResourceNotFoundException,
      ExpiredTokenException,
      ConfirmedException {
    Token confirmationToken = tokenService.findByToken(token);
    LocalDateTime expiredAt = confirmationToken.getExpiredAt();

    if (expiredAt.isBefore(LocalDateTime.now())) {
      throw new ExpiredTokenException(ErrorMessage.EXPIRED_TOKEN);
    }

    if (confirmationToken.getConfirmAt() != null) {
      throw new ConfirmedException(ErrorMessage.CONFIRMED);
    }

    confirmationToken.setConfirmAt(LocalDateTime.now());
    tokenService.save(confirmationToken);

    User user = confirmationToken.getUser();
    user.setEnabled(true);
    userRepository.save(user);
  }

  @Override
  public void reconfirm(String email) throws ResourceNotFoundException, ConfirmedException {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));

    if (user.isEnabled()) {
      throw new ConfirmedException(ErrorMessage.CONFIRMED);
    }

    Token token = tokenService.create(user);

    String apiEndPoint = "/api/auth/confirm/";
    String url = backendUrl + apiEndPoint + token.getToken();
    emailService.sendConfirm(user.getEmail(), user.getUsername(), url);
  }

  @Override
  public AuthInfo login(LoginRequest req)
      throws UnconfirmedException,
      InvalidCredentialsException {
    User user = userRepository.findByEmail(req.email())
        .orElseThrow(() -> new InvalidCredentialsException(ErrorMessage.INVALID_CREDENTIALS));

    if (!passwordEncoder.matches(req.password(), user.getPassword())) {
      throw new InvalidCredentialsException(ErrorMessage.INVALID_CREDENTIALS);
    }

    if (!user.isEnabled()) {
      throw new UnconfirmedException(ErrorMessage.UNCONFIRMED);
    }

    return new AuthInfo(
        jwtService.generateToken(user.getEmail(), JwtConstants.ACCESS_TOKEN_EXP, accessTokenSecret),
        jwtService.generateToken(user.getEmail(), JwtConstants.REFRESH_TOKEN_EXP, refreshTokenSecret));
  }

  @Override
  public AuthInfo googleOAuth2Login(String idTokenString)
      throws InvalidCredentialsException,
      EmailTakenException,
      GeneralSecurityException,
      IOException {
    GoogleIdToken idToken = googleVerifier.verify(idTokenString);
    if (idToken == null) {
      throw new InvalidCredentialsException(ErrorMessage.INVALID_CREDENTIALS);
    }

    Payload payload = idToken.getPayload();
    String email = payload.getEmail();

    User user;
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isEmpty()) {
      String username = (String) payload.get("given_name");
      final String password = "";
      final String phoneNumber = "";
      try {
        user = userRepository.save(new User(email, password, phoneNumber, username, Roles.ROLE_USER));
      } catch (Exception e) {
        throw new EmailTakenException(ErrorMessage.EMAIL_TAKEN);
      }
    } else {
      user = optionalUser.get();
    }

    return new AuthInfo(
        jwtService.generateToken(user.getEmail(), JwtConstants.ACCESS_TOKEN_EXP, accessTokenSecret),
        jwtService.generateToken(user.getEmail(), JwtConstants.REFRESH_TOKEN_EXP, refreshTokenSecret));
  }

  @Override
  public AuthInfo refreshToken(String refresh_token) throws UnauthenticatedException {
    if (jwtService.isTokenExpired(refresh_token, refreshTokenSecret)) {
      throw new UnauthenticatedException(ErrorMessage.UNAUTHENTICATED);
    }
    String email = jwtService.extractUsername(refresh_token, refreshTokenSecret);
    return new AuthInfo(
        jwtService.generateToken(email, JwtConstants.ACCESS_TOKEN_EXP, accessTokenSecret),
        refresh_token);
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    for (Cookie cookie : request.getCookies()) {
      String cookieName = cookie.getName();
      Cookie cookieToDelete = new Cookie(cookieName, null);
      cookieToDelete.setMaxAge(0);
      response.addCookie(cookieToDelete);
    }
  }

  @Override
  public void forgot(String email) throws ResourceNotFoundException, UnconfirmedException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));

    if (!user.isEnabled()) {
      throw new UnconfirmedException(ErrorMessage.UNCONFIRMED);
    }

    Token token = tokenService.create(user);
    final String endPoint = "/reset/";
    String url = frontendUrl + endPoint + token.getToken();
    emailService.sendReset(user.getEmail(), user.getUsername(), url);
  }

  @Override
  public void reset(String token,
      String password,
      String confirmPassword) throws ResourceNotFoundException,
      InvalidCredentialsException {

    if (!password.equals(confirmPassword)) {
      throw new InvalidCredentialsException(ErrorMessage.INVALID_CREDENTIALS);
    }

    Token tokenReset = tokenService.findByToken(token);

    User user = tokenReset.getUser();
    String passwordEncode = passwordEncoder.encode(password);
    user.setPassword(passwordEncode);
    userRepository.save(user);
  }

  private String signUp(User user) {
    String encodePassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodePassword);
    User registeredUser = userRepository.save(user);

    Token token = tokenService.create(registeredUser);

    return token.getToken();
  }
}
