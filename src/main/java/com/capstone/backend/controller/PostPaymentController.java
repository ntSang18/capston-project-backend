package com.capstone.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.backend.dto.postPayment.PostPaymentRequest;
import com.capstone.backend.dto.postPayment.PostPaymentResponse;
import com.capstone.backend.exception.DifferentTypeException;
import com.capstone.backend.exception.InsufficientBalanceException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.exception.UnqualifiedException;
import com.capstone.backend.model.UserDetailsImpl;
import com.capstone.backend.service.iservice.IPostPaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post-payment")
public class PostPaymentController {

  private final IPostPaymentService postPaymentService;

  @GetMapping(value = "")
  public ResponseEntity<?> getPostPayments() {
    List<PostPaymentResponse> responses = postPaymentService.getPostPayments();
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @GetMapping(value = "/personal")
  public ResponseEntity<?> getPersonalPostPayments(@AuthenticationPrincipal UserDetailsImpl user) {
    List<PostPaymentResponse> responses = postPaymentService.getPersonalPostPayments(user.getUsername());
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getPostPayment(@PathVariable long id)
      throws ResourceNotFoundException {
    PostPaymentResponse response = postPaymentService.getPostPayment(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "")
  public ResponseEntity<?> purchase(
      @AuthenticationPrincipal UserDetailsImpl user,
      @RequestBody PostPaymentRequest request)
      throws ResourceNotFoundException, 
      InsufficientBalanceException, 
      DifferentTypeException, 
      UnqualifiedException {
    PostPaymentResponse response = postPaymentService.purchase(user.getUsername(), request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deletePostPayment(@PathVariable long id)
      throws ResourceNotFoundException {
    postPaymentService.deletePostPayment(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
