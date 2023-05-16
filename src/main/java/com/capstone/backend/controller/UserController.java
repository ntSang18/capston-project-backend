package com.capstone.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.backend.dto.CreateUserRequest;
import com.capstone.backend.dto.UpdateUserRequest;
import com.capstone.backend.dto.UserResponse;
import com.capstone.backend.exception.EmailTakenException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.UserDetailsImpl;
import com.capstone.backend.service.iservice.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {
  private final IUserService userService;

  @GetMapping(value = "")
  public ResponseEntity<?> getUsers() {
    List<UserResponse> userResponses = userService.getUsers();
    return new ResponseEntity<>(userResponses, HttpStatus.OK);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getUser(@PathVariable long id)
      throws ResourceNotFoundException {
    UserResponse userResponse = userService.getUser(id);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @GetMapping(value = "/current")
  public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl user)
      throws ResourceNotFoundException {
    UserResponse userResponse = userService.getCurrentUser(user.getUsername());
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @PostMapping(value = "")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> createUser(@RequestBody CreateUserRequest userRequest)
      throws EmailTakenException {
    UserResponse userResponse = userService.createUser(userRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @PatchMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<?> updateUser(@ModelAttribute UpdateUserRequest userRequest)
      throws ResourceNotFoundException {
    UserResponse userResponse = userService.updateUser(userRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable long id) throws ResourceNotFoundException {
    userService.deleteUser(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
