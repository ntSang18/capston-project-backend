package com.capstone.backend.dto.postPayment;

import java.time.LocalDateTime;

import com.capstone.backend.dto.packagePrice.PackagePriceResponse;
import com.capstone.backend.dto.post.PostResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PostPaymentResponse(
    long id,
    @JsonProperty("number_package") int numberPackage,
    @JsonProperty("paid_at") LocalDateTime paidAt,
    PostResponse post,
    @JsonProperty("package_price") PackagePriceResponse packagePrice) {
}
