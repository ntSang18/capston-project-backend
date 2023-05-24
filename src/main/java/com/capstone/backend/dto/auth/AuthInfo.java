package com.capstone.backend.dto.auth;

public record AuthInfo(
    String token,
    String refreshToken) {
}
