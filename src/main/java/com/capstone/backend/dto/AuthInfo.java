package com.capstone.backend.dto;

public record AuthInfo(
    String token,
    String refreshToken) {
}
