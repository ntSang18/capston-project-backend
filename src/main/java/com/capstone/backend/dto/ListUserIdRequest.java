package com.capstone.backend.dto;

import java.util.List;

public record ListUserIdRequest(
    List<Long> ids) {
}
