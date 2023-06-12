package com.capstone.backend.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.user.UserResponse;
import com.capstone.backend.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserResponseMapper implements Function<User, UserResponse> {

  private final AddressDtoMapper addressDtoMapper;

  @Override
  public UserResponse apply(User user) {
    return new UserResponse(
        user.getId(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getUsername(),
        user.getFacebook(),
        user.getImageUrl(),
        user.getRole().name(),
        user.getBalance(),
        user.isLocked(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        addressDtoMapper.apply(user.getAddress()));
  }

}
