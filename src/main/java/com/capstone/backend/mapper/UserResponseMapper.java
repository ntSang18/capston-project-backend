package com.capstone.backend.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.UserResponse;
import com.capstone.backend.model.User;

@Service
public class UserResponseMapper implements Function<User, UserResponse> {

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
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getAddress().getProvince(),
        user.getAddress().getProvince(),
        user.getAddress().getCommune(),
        user.getAddress().getSpecificAddress());
  }

}
