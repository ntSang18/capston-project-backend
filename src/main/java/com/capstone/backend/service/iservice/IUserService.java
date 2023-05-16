package com.capstone.backend.service.iservice;

import java.util.List;

import com.capstone.backend.dto.CreateUserRequest;
import com.capstone.backend.dto.UpdateUserRequest;
import com.capstone.backend.dto.UserResponse;
import com.capstone.backend.exception.EmailTakenException;
import com.capstone.backend.exception.ResourceNotFoundException;

public interface IUserService {
  List<UserResponse> getUsers();

  UserResponse getUser(long id) throws ResourceNotFoundException;

  UserResponse getCurrentUser(String email) throws ResourceNotFoundException;

  UserResponse createUser(CreateUserRequest userRequest) throws EmailTakenException;

  UserResponse updateUser(UpdateUserRequest userRequest) throws ResourceNotFoundException;

  void deleteUser(long id) throws ResourceNotFoundException;
}
