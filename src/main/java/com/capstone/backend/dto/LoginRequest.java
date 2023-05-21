package com.capstone.backend.dto;

public record LoginRequest(
        String email,
        String password,
        String role) {
}
