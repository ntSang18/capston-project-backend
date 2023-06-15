package com.capstone.backend.dto.promotion;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PromotionRequest(
    String name,
    @JsonProperty("start_at") LocalDateTime startAt,
    @JsonProperty("end_at") LocalDateTime endAt,
    List<PromotionChildDto> childs) {
}
