package com.capstone.backend.service.serviceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.backend.constant.Roles;
import com.capstone.backend.dto.common.ListIdRequest;
import com.capstone.backend.dto.user.CreateUserRequest;
import com.capstone.backend.dto.user.ListUserResponse;
import com.capstone.backend.dto.user.UpdateUserRequest;
import com.capstone.backend.dto.user.UserResponse;
import com.capstone.backend.exception.ResourceAlreadyExists;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.mapper.UserResponseMapper;
import com.capstone.backend.model.Address;
import com.capstone.backend.model.User;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.service.iservice.IAddressService;
import com.capstone.backend.service.iservice.IFileService;
import com.capstone.backend.service.iservice.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

  private final UserRepository userRepository;

  private final IAddressService addressService;

  private final UserResponseMapper userResponseMapper;

  private final PasswordEncoder passwordEncoder;

  private final IFileService fileService;

  private final MessageSource messageSource;

  @Override
  public User findById(long id) throws ResourceNotFoundException {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));

    if (user.isDeleted()) {
      throw new ResourceNotFoundException(
          messageSource.getMessage("error.resource-not-found", null, Locale.getDefault()));
    }

    return user;
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public User findByEmail(String email) throws ResourceNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));

    if (user.isDeleted()) {
      throw new ResourceNotFoundException(
          messageSource.getMessage("error.resource-not-found", null, Locale.getDefault()));
    }

    return user;
  }

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
    return userResponseMapper.apply(findById(id));
  }

  @Override
  public UserResponse getCurrentUser(String email) throws ResourceNotFoundException {
    return userResponseMapper.apply(findByEmail(email));
  }

  @Override
  public UserResponse createUser(CreateUserRequest userRequest) throws ResourceAlreadyExists {

    if (userRepository.findByEmail(userRequest.email()).isPresent()) {
      throw new ResourceAlreadyExists(messageSource.getMessage(
          "error.resource-already-exists",
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
  public UserResponse updateUser(long id, UpdateUserRequest userRequest) throws ResourceNotFoundException {
    User user = findById(id);

    userRequest.phone_number().ifPresent(phoneNumber -> user.setPhoneNumber(phoneNumber));
    userRequest.username().ifPresent(username -> user.setUsername(username));
    userRequest.facebook().ifPresent(facebook -> user.setFacebook(facebook));
    userRequest.role().ifPresent(role -> user.setRole(Roles.valueOf(role)));

    String folder = "users";
    userRequest.image().ifPresent(
        image -> fileService.store(folder, user.getId(), image)
            .ifPresent(imageUrl -> user.setImageUrl(imageUrl)));

    Address address = user.getAddress();
    address.setProvince(userRequest.province());
    address.setDistrict(userRequest.district());
    address.setWard(userRequest.ward());
    address.setSpecificAddress(userRequest.specific_address());
    addressService.save(address);

    return userResponseMapper.apply(userRepository.save(user));
  }

  @Override
  public void lockUser(ListIdRequest ids) throws ResourceNotFoundException {
    for (long id : ids.ids()) {
      User user = findById(id);
      user.setLocked(true);
      userRepository.save(user);
    }
  }

  @Override
  public void unlockUser(ListIdRequest ids)
      throws ResourceNotFoundException {
    for (long id : ids.ids()) {
      User user = findById(id);
      user.setLocked(false);
      userRepository.save(user);
    }
  }

  @Override
  public void deleteUser(ListIdRequest ids) throws ResourceNotFoundException {
    for (long id : ids.ids()) {
      User user = findById(id);
      user.setDeleted(true);
      userRepository.save(user);
    }
  }
}
