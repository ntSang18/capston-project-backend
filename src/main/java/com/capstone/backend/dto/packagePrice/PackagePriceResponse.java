package com.capstone.backend.dto.packagePrice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PackagePriceResponse(
    long id,
    String name,
    long price,
    String type,
    @JsonProperty("number_of_days") int numberOfDay,
    @JsonProperty("total_purchase") long totalPurchase) {
}
