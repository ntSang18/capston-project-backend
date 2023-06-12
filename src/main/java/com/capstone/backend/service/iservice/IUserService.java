package com.capstone.backend.service.iservice;

import com.capstone.backend.dto.common.ListIdRequest;
import com.capstone.backend.dto.user.CreateUserRequest;
import com.capstone.backend.dto.user.ListUserResponse;
import com.capstone.backend.dto.user.UpdateUserRequest;
import com.capstone.backend.dto.user.UserResponse;
import com.capstone.backend.exception.ResourceAlreadyExists;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.User;

public interface IUserService {

  User findById(long id) throws ResourceNotFoundException;

  User save(User user);

  User findByEmail(String email) throws ResourceNotFoundException;

  ListUserResponse getUsers();

  UserResponse getUser(long id) throws ResourceNotFoundException;

  UserResponse getCurrentUser(String email) throws ResourceNotFoundException;

  UserResponse createUser(CreateUserRequest userRequest) throws ResourceAlreadyExists;

  UserResponse updateUser(long id, UpdateUserRequest userRequest) throws ResourceNotFoundException;

  void lockUser(ListIdRequest ids) throws ResourceNotFoundException;

  void deleteUser(ListIdRequest ids) throws ResourceNotFoundException;

  void unlockUser(ListIdRequest ids) throws ResourceNotFoundException;
}
