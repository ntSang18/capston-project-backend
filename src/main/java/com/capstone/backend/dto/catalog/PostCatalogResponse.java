package com.capstone.backend.dto.catalog;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostCatalogResponse(
    long id,
    String name,
    @JsonProperty("created_at") LocalDateTime createdAt,
    @JsonProperty("total_count") long totalCount,
    @JsonProperty("total_public_post") long totalPublicPost,
    @JsonProperty("total_expired_post") long totalExpiredPost,
    @JsonProperty("total_denied_post") long totalDeniedPost,
    @JsonProperty("total_unpaid_post") long totalUnpaidPost,
    @JsonProperty("total_unconfirmed_post") long totalUnconfirmedPost) {

}
