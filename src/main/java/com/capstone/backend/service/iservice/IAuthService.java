package com.capstone.backend.service.iservice;

import java.io.IOException;
import java.security.GeneralSecurityException;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {

  void register(RegisterRequest req) throws EmailTakenException;

  void confirm(String token) throws ResourceNotFoundException, ExpiredTokenException, ConfirmedException;

  void reconfirm(String email) throws ResourceNotFoundException, ConfirmedException;

  AuthInfo login(LoginRequest req)
      throws UnconfirmedException, InvalidCredentialsException;

  AuthInfo googleOAuth2Login(String idTokenString)
      throws InvalidCredentialsException, EmailTakenException, GeneralSecurityException, IOException;

  AuthInfo refreshToken(String refresh_token) throws UnauthenticatedException;

  void logout(HttpServletRequest request, HttpServletResponse response);

  void forgot(String email) throws ResourceNotFoundException, UnconfirmedException;

  void reset(String token, String password, String confirmPassword)
      throws ResourceNotFoundException,
      InvalidCredentialsException;

}
