package com.capstone.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResetRequest(
        String token,
        String password,
        @JsonProperty("confirm_password") String confirmPassword) {

}
