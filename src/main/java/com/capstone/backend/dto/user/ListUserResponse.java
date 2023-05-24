package com.capstone.backend.dto.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ListUserResponse(
    List<UserResponse> users,
    @JsonProperty("total_user") long totalUser,
    @JsonProperty("total_admin") long totalAdmin) {
}
