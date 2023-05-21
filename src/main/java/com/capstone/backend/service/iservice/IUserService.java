package com.capstone.backend.service.iservice;

import com.capstone.backend.dto.CreateUserRequest;
import com.capstone.backend.dto.ListUserIdRequest;
import com.capstone.backend.dto.ListUserResponse;
import com.capstone.backend.dto.UpdateUserRequest;
import com.capstone.backend.dto.UserResponse;
import com.capstone.backend.exception.EmailTakenException;
import com.capstone.backend.exception.ResourceNotFoundException;

public interface IUserService {
  ListUserResponse getUsers();

  UserResponse getUser(long id) throws ResourceNotFoundException;

  UserResponse getCurrentUser(String email) throws ResourceNotFoundException;

  UserResponse createUser(CreateUserRequest userRequest) throws EmailTakenException;

  UserResponse updateUser(UpdateUserRequest userRequest) throws ResourceNotFoundException;

  void lockUser(ListUserIdRequest ids) throws ResourceNotFoundException;

  void deleteUser(ListUserIdRequest ids) throws ResourceNotFoundException;

  void unlockUser(ListUserIdRequest ids) throws ResourceNotFoundException;
}
