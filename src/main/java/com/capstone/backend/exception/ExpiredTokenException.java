package com.capstone.backend.exception;

public class ExpiredTokenException extends Exception {

  public ExpiredTokenException(String message) {
    super(message);
  }

}
