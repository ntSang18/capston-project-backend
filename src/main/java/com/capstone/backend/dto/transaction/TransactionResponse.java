package com.capstone.backend.dto.transaction;

import java.time.LocalDateTime;

import com.capstone.backend.dto.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionResponse(
    long id,
    @JsonProperty("created_at") LocalDateTime createdAt,
    long money,
    @JsonProperty("actual_money") long actualMoney,
    String method,
    double discount,
    UserResponse user) {
}
