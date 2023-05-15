package com.capstone.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Oauth2Request(@JsonProperty("token") String idToken) {

}
