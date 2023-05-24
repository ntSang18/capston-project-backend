package com.capstone.backend.dto.user;

import java.util.List;

public record ListUserIdRequest(
    List<Long> ids) {
}
