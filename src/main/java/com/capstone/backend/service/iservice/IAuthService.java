package com.capstone.backend.service.iservice;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.capstone.backend.dto.auth.AuthInfo;
import com.capstone.backend.dto.auth.ChangePasswordRequest;
import com.capstone.backend.dto.auth.LoginRequest;
import com.capstone.backend.dto.auth.RegisterRequest;
import com.capstone.backend.exception.ConfirmedException;
import com.capstone.backend.exception.ResourceAlreadyExists;
import com.capstone.backend.exception.ExpiredTokenException;
import com.capstone.backend.exception.InvalidCredentialsException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.exception.UnauthenticatedException;
import com.capstone.backend.exception.AccountDisableException;
import com.capstone.backend.exception.AccountLockedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {

  void register(RegisterRequest req) throws ResourceAlreadyExists;

  void confirm(String token) throws ResourceNotFoundException, ExpiredTokenException, ConfirmedException;

  void reconfirm(String email) throws ResourceNotFoundException, ConfirmedException;

  AuthInfo login(LoginRequest req)
      throws AccountDisableException, InvalidCredentialsException, AccountLockedException;

  AuthInfo googleOAuth2Login(String idTokenString)
      throws InvalidCredentialsException, ResourceAlreadyExists, GeneralSecurityException, IOException;

  AuthInfo refreshToken(String refresh_token) throws UnauthenticatedException;

  void logout(HttpServletRequest request, HttpServletResponse response);

  void forgot(String email) throws ResourceNotFoundException, AccountDisableException;

  void reset(String token, String password, String confirmPassword)
      throws ResourceNotFoundException,
      InvalidCredentialsException;

  void change(String email, ChangePasswordRequest request)
      throws ResourceNotFoundException, InvalidCredentialsException;

}
