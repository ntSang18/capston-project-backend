package com.capstone.backend.dto.auth;

public record LoginRequest(
        String email,
        String password,
        String role) {
}
