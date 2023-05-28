package com.capstone.backend.dto.post;

import org.springframework.web.multipart.MultipartFile;

public record CreatePublicPostRequest(
    String title,
    String description,
    long price,
    long deposit,
    String target,
    String type,
    long acreage,
    String province,
    String district,
    String ward,
    String specific_address,
    MultipartFile[] images,
    String expired_at,
    long catalog_id,
    long user_id) {
}
