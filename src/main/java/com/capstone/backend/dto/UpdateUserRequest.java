package com.capstone.backend.dto;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateUserRequest(
    long id,
    @JsonProperty("phone_number") Optional<String> phoneNumber,
    Optional<String> username,
    Optional<String> facebook,
    Optional<MultipartFile> image,
    Optional<String> role,
    Optional<String> province,
    Optional<String> district,
    Optional<String> commune,
    @JsonProperty("specific_address") Optional<String> specificAddress) {

}
