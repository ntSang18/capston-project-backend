package com.capstone.backend.dto.packagePrice;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PackagePriceRequest(
    String name,
    long price,
    @JsonProperty("number_of_days") int numberOfDays,
    String type) {
}
