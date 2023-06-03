package com.capstone.backend.dto.postPayment;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostPaymentRequest(
    @JsonProperty("post_id") long postId,
    @JsonProperty("package_price_id") long packagePriceId,
    @JsonProperty("number_package") int numberPackage) {
}
