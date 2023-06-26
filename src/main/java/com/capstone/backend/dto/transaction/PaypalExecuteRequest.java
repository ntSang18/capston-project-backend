package com.capstone.backend.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaypalExecuteRequest(
    @JsonProperty("payment_id") String paymentId,
    @JsonProperty("payer_id") String payerId) {
}
