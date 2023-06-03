package com.capstone.backend.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import com.capstone.backend.dto.address.AddressDto;
import com.capstone.backend.dto.catalog.PostCatalogDto;
import com.capstone.backend.dto.media.PostMediaDto;
import com.capstone.backend.dto.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PostResponse(
    long id,
    String title,
    String description,
    long price,
    long deposit,
    String target,
    long acreage,
    String type,
    String status,
    @JsonProperty("refused_reason") String refusedReason,
    @JsonProperty("created_at") LocalDateTime createdAt,
    @JsonProperty("confirmed") LocalDateTime confirmedAt,
    @JsonProperty("declined_at") LocalDateTime declinedAt,
    @JsonProperty("paid_at") LocalDateTime paidAt,
    @JsonProperty("expired_at") LocalDateTime expiredAt,
    UserResponse user,
    AddressDto address,
    PostCatalogDto catalog,
    List<PostMediaDto> medias) {
}
