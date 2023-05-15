package com.capstone.backend.dto;

public record ErrorResponse(
    int status,
    String error,
    String message) {

}
