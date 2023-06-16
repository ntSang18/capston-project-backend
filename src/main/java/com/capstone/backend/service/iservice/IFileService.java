package com.capstone.backend.service.iservice;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
  Optional<String> store(String folder, long id, MultipartFile file);

  Optional<String> cloudinaryStore(MultipartFile file);
}
