package com.capstone.backend.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DenyPostRequest(@JsonProperty("refused_reason") String refusedReason) {

}
