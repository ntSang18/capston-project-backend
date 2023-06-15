package com.capstone.backend.service.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.backend.service.iservice.IFileService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements IFileService {

  private final Storage storage;

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
}
