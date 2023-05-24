package com.capstone.backend.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import com.capstone.backend.dto.address.AddressDto;
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
    LocalDateTime createdAt,
    LocalDateTime confirmedAt,
    LocalDateTime declinedAt,
    LocalDateTime paidAt,
    LocalDateTime expiredAt,
    UserResponse user,
    AddressDto address,
    @JsonProperty("image_url") List<String> imageUrls) {

}
