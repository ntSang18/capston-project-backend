package com.capstone.backend.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.backend.constant.ErrorMessage;
import com.capstone.backend.constant.Roles;
import com.capstone.backend.dto.CreateUserRequest;
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

  @Override
  public UserResponse createUser(CreateUserRequest userRequest) throws EmailTakenException {

    if (userRepository.findByEmail(userRequest.email()).isPresent()) {
      throw new EmailTakenException(ErrorMessage.EMAIL_TAKEN);
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
  public void deleteUser(long id) throws ResourceNotFoundException {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));

    user.setDeleted(true);
    userRepository.save(user);
  }

  @Override
  public UserResponse getUser(long id) throws ResourceNotFoundException {
    return userRepository
        .findById(id)
        .filter(user -> !user.isDeleted())
        .map(userResponseMapper)
        .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
  }

  @Override
  public UserResponse getCurrentUser(String email) throws ResourceNotFoundException {
    return userRepository
        .findByEmail(email)
        .map(userResponseMapper)
        .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));
  }

  @Override
  public List<UserResponse> getUsers() {
    List<UserResponse> users = userRepository.findAll()
        .stream()
        .filter(user -> !user.isDeleted() && user.isEnabled())
        .map(userResponseMapper)
        .collect(Collectors.toList());
    return users;
  }

  @Override
  public UserResponse updateUser(UpdateUserRequest userRequest) throws ResourceNotFoundException {
    User user = userRepository.findById(userRequest.id())
        .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND));

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
    userRequest.commune().ifPresent(commune -> address.setCommune(commune));
    userRequest.specificAddress()
        .ifPresent(specificAddress -> address.setSpecificAddress(specificAddress));
    addressRepository.save(address);

    return userResponseMapper.apply(userRepository.save(user));
  }

}
