package com.capstone.backend.advice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.capstone.backend.dto.ExceptionResponse;
import com.capstone.backend.exception.AccountDisableException;
import com.capstone.backend.exception.AccountLockedException;
import com.capstone.backend.exception.ConfirmedException;
import com.capstone.backend.exception.EmailTakenException;
import com.capstone.backend.exception.ExpiredTokenException;
import com.capstone.backend.exception.InvalidCredentialsException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.exception.UnauthenticatedException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

  @Value("${application.server.time-zone}")
  private String timeZone;

  @ExceptionHandler(value = { ConfirmedException.class })
  public ResponseEntity<?> handleException(ConfirmedException e) {
    return generateResponse(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = { EmailTakenException.class })
  public ResponseEntity<?> handleException(EmailTakenException e) {
    return generateResponse(e, HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(value = { ExpiredTokenException.class })
  public ResponseEntity<?> handleException(ExpiredTokenException e) {
    return generateResponse(e, HttpStatus.GONE);
  }

  @ExceptionHandler(value = { InvalidCredentialsException.class })
  public ResponseEntity<?> handleException(InvalidCredentialsException e) {
    return generateResponse(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = { ResourceNotFoundException.class })
  public ResponseEntity<?> handleException(ResourceNotFoundException e) {
    return generateResponse(e, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = { UnauthenticatedException.class })
  public ResponseEntity<?> handleException(UnauthenticatedException e) {
    return generateResponse(e, HttpStatus.PAYMENT_REQUIRED);
  }

  @ExceptionHandler(value = { AccountDisableException.class })
  public ResponseEntity<?> handleException(AccountDisableException e) {
    return generateResponse(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = { Exception.class })
  public ResponseEntity<?> handleException(Exception e) {
    return generateResponse(e, HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler(value = { AccountLockedException.class })
  public ResponseEntity<?> handleException(AccountLockedException e) {
    return generateResponse(e, HttpStatus.LOCKED);
  }

  private ResponseEntity<?> generateResponse(Exception e, HttpStatus status) {
    log.error(e.getMessage());
    ZoneId zoneId = ZoneId.of(timeZone);
    ExceptionResponse response = new ExceptionResponse(
        e.getMessage(),
        status.value(),
        ZonedDateTime.now(zoneId));
    return new ResponseEntity<>(response, status);
  }

}
