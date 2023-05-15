package com.capstone.backend.service.iservice;

import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.Token;
import com.capstone.backend.model.User;

public interface ITokenService {

  Token save(Token token);

  Token create(User user);

  Token findByToken(String token) throws ResourceNotFoundException;

  Token setConfirmedAt(Token token);
}
