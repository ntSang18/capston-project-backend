package com.capstone.backend.dto.user;

import java.time.LocalDateTime;

import com.capstone.backend.dto.address.AddressDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
    long id,
    String email,
    @JsonProperty("phone_number") String phoneNumber,
    String username,
    String facebook,
    @JsonProperty("image_url") String imageUrl,
    String role,
    long balance,
    boolean locked,
    @JsonProperty("created_at") LocalDateTime createdAt,
    @JsonProperty("updated_at") LocalDateTime updatedAt,
    AddressDto address) {
}
