package com.capstone.backend.service.serviceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.backend.constant.Roles;
import com.capstone.backend.dto.CreateUserRequest;
import com.capstone.backend.dto.ListUserIdRequest;
import com.capstone.backend.dto.ListUserResponse;
import com.capstone.backend.dto.UpdateUserRequest;
import com.capstone.backend.dto.UserResponse;
import com.capstone.backend.exception.EmailTakenException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.mapper.UserResponseMapper;
import com.capstone.backend.model.Address;
import com.capstone.backend.model.User;
import com.capstone.backend.repository.AddressRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.service.iservice.IFileService;
import com.capstone.backend.service.iservice.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

  private final UserRepository userRepository;

  private final AddressRepository addressRepository;

  private final UserResponseMapper userResponseMapper;

  private final PasswordEncoder passwordEncoder;

  private final IFileService fileService;

  private final MessageSource messageSource;

  @Override
  public ListUserResponse getUsers() {
    List<UserResponse> users = userRepository.findAll()
        .stream()
        .filter(user -> !user.isDeleted() && user.isEnabled())
        .sorted(Comparator.comparingLong(User::getId))
        .map(userResponseMapper)
        .collect(Collectors.toList());

    long totalUser = users
        .stream()
        .filter(userResponse -> userResponse.role().equals(Roles.ROLE_USER.name()))
        .count();

    long totalAdmin = users.size() - totalUser;
    return new ListUserResponse(users, totalUser, totalAdmin);
  }

  @Override
  public UserResponse getUser(long id) throws ResourceNotFoundException {
    return userRepository
        .findById(id)
        .filter(user -> !user.isDeleted())
        .map(userResponseMapper)
        .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
            "error.resource-not-found",
            null,
            Locale.getDefault())));
  }

  @Override
  public UserResponse getCurrentUser(String email) throws ResourceNotFoundException {
    return userRepository
        .findByEmail(email)
        .map(userResponseMapper)
        .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
            "error.resource-not-found",
            null,
            Locale.getDefault())));
  }

  @Override
  public UserResponse createUser(CreateUserRequest userRequest) throws EmailTakenException {

    if (userRepository.findByEmail(userRequest.email()).isPresent()) {
      throw new EmailTakenException(messageSource.getMessage(
          "error.resource-not-found",
          null,
          Locale.getDefault()));
    }

    User user = new User(
        userRequest.email(),
        passwordEncoder.encode(userRequest.password()),
        userRequest.phoneNumber(),
        userRequest.username(),
        Roles.valueOf(userRequest.role()));
    user.setEnabled(true);

    return userResponseMapper.apply(userRepository.save(user));
  }

  @Override
  public UserResponse updateUser(UpdateUserRequest userRequest) throws ResourceNotFoundException {
    User user = userRepository.findById(userRequest.id())
        .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
            "error.resource-not-found",
            null,
            Locale.getDefault())));

    userRequest.phoneNumber().ifPresent(phoneNumber -> user.setPhoneNumber(phoneNumber));
    userRequest.username().ifPresent(username -> user.setUsername(username));
    userRequest.facebook().ifPresent(facebook -> user.setFacebook(facebook));
    userRequest.role().ifPresent(role -> user.setRole(Roles.valueOf(role)));

    String folder = "users";
    userRequest.image().ifPresent(
        image -> fileService.store(folder, user.getId(), image)
            .ifPresent(imageUrl -> user.setImageUrl(imageUrl)));

    Address address = user.getAddress();
    userRequest.province().ifPresent(province -> address.setProvince(province));
    userRequest.district().ifPresent(district -> address.setDistrict(district));
    userRequest.ward().ifPresent(ward -> address.setWard(ward));
    userRequest.specific_address()
        .ifPresent(specificAddress -> address.setSpecificAddress(specificAddress));
    addressRepository.save(address);

    return userResponseMapper.apply(userRepository.save(user));
  }

  @Override
  public void lockUser(ListUserIdRequest ids) throws ResourceNotFoundException {
    for (long id : ids.ids()) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
              "error.resource-not-found",
              null,
              Locale.getDefault())));
      user.setLocked(true);
      userRepository.save(user);
    }
  }

  @Override
  public void unlockUser(ListUserIdRequest ids)
      throws ResourceNotFoundException {
    for (long id : ids.ids()) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
              "error.resource-not-found",
              null,
              Locale.getDefault())));
      user.setLocked(false);
      userRepository.save(user);
    }
  }

  @Override
  public void deleteUser(ListUserIdRequest ids) throws ResourceNotFoundException {
    for (long id : ids.ids()) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException(messageSource.getMessage(
              "error.resource-not-found",
              null,
              Locale.getDefault())));
      user.setDeleted(true);
      userRepository.save(user);
    }
  }

}
