package com.capstone.backend.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.Token;
import com.capstone.backend.model.User;
import com.capstone.backend.repository.TokenRepository;
import com.capstone.backend.service.iservice.ITokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

  private final TokenRepository tokenRepository;

  @Override
  public Token save(Token token) {
    return tokenRepository.save(token);
  }

  @Override
  public Token create(User user) {
    String token = UUID.randomUUID().toString();
    Token confirmationToken = new Token(
        token,
        LocalDateTime.now(),
        LocalDateTime.now().plusMinutes(15),
        user);
    return tokenRepository.save(confirmationToken);
  }

  @Override
  public Token findByToken(String token) throws ResourceNotFoundException {
    return tokenRepository
        .findByToken(token)
        .orElseThrow(() -> new ResourceNotFoundException("Token does not exist"));
  }

  @Override
  public Token setConfirmedAt(Token token) {
    token.setConfirmAt(LocalDateTime.now());
    return tokenRepository.save(token);
  }
}
