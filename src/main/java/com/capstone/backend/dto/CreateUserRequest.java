package com.capstone.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUserRequest(
    String username,
    String email,
    @JsonProperty("phone_number") String phoneNumber,
    String password,
    String role) {

}
