package com.capstone.backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResetRequest(
        String token,
        String password,
        @JsonProperty("confirm_password") String confirmPassword) {

}
