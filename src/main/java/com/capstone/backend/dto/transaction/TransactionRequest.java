package com.capstone.backend.dto.transaction;

public record TransactionRequest(
    long money,
    String method) {
}
