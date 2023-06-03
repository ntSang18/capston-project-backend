package com.capstone.backend.dto.post;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public record CreatePostRequest(
    String title,
    String description,
    long price,
    long deposit,
    String target,
    Optional<String> type,
    long acreage,
    String province,
    String district,
    String ward,
    String specific_address,
    MultipartFile[] images,
    Optional<String> expired_at,
    long catalog_id,
    long user_id) {
}
