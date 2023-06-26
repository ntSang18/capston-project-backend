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

import com.capstone.backend.dto.transaction.PaypalExecuteRequest;
import com.capstone.backend.dto.transaction.TransactionRequest;
import com.capstone.backend.dto.transaction.TransactionResponse;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.UserDetailsImpl;
import com.capstone.backend.service.iservice.ITransactionService;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/transaction")
public class TransactionController {

  private final ITransactionService transactionService;

  @GetMapping(value = "")
  public ResponseEntity<?> getTransactions() {
    List<TransactionResponse> responses = transactionService.getTransactions();
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @GetMapping(value = "/personal")
  public ResponseEntity<?> getPersonalTransactions(@AuthenticationPrincipal UserDetailsImpl user)
      throws ResourceNotFoundException {
    List<TransactionResponse> responses = transactionService.getPersonalTransactions(user.getUsername());
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getTransaction(@PathVariable long id)
      throws ResourceNotFoundException {
    TransactionResponse response = transactionService.getTransaction(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "")
  public ResponseEntity<?> createTransaction(
      @AuthenticationPrincipal UserDetailsImpl user,
      @RequestBody TransactionRequest request)
      throws ResourceNotFoundException {
    TransactionResponse response = transactionService.createTransaction(user.getUsername(), request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/execute")
  public ResponseEntity<?> execute(
      @AuthenticationPrincipal UserDetailsImpl user,
      @RequestBody PaypalExecuteRequest request)
      throws PayPalRESTException, ResourceNotFoundException {
    TransactionResponse response = transactionService.executePaypal(user.getUsername(), request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deleteTransaction(@PathVariable long id)
      throws ResourceNotFoundException {
    transactionService.deleteTransaction(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
