package com.capstone.backend.service.serviceImpl;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.backend.service.iservice.IFileService;
import com.cloudinary.Cloudinary;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService {

  private final Storage storage;

  private final Cloudinary cloudinary;

  @Value("${application.storage.bucket}")
  private String bucket;

  @Value("${application.storage.baseUrl}")
  private String baseUrl;

  @Override
  public Optional<String> store(String folder, long id, MultipartFile file) {
    final String fileName = file.getOriginalFilename();
    final String contentType = file.getContentType();
    try {
      BlobId blobId = BlobId.of(bucket, String.format("%s/%s/%s", folder, id, fileName));
      BlobInfo info = BlobInfo.newBuilder(blobId)
          .setContentType(contentType)
          .build();
      byte[] bytes = file.getInputStream().readAllBytes();
      Blob blob = storage.create(info, bytes);
      String imageUrl = String.format(baseUrl + "%s/%s", blob.getBucket(), blob.getName());
      return Optional.of(imageUrl);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public Optional<String> cloudinaryStore(MultipartFile file) {
    try {
      String imageUrl = cloudinary.uploader()
          .upload(file.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
          .get("url")
          .toString();
      return Optional.of(imageUrl);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}
