package com.capstone.backend.dto.user;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserRequest(
    Optional<String> phone_number,
    Optional<String> username,
    Optional<String> facebook,
    Optional<MultipartFile> image,
    Optional<String> role,
    String province,
    String district,
    String ward,
    String specific_address) {
}