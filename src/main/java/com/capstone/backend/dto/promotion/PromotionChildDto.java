package com.capstone.backend.dto.promotion;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PromotionChildDto(
    Optional<Long> id,
    @JsonProperty("start_range") long startRange,
    @JsonProperty("end_range") Optional<Long> endRange,
    int percent) {
}
