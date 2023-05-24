package com.capstone.backend.service.serviceImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.backend.model.PostMedia;
import com.capstone.backend.repository.PostMediaRepository;
import com.capstone.backend.service.iservice.IFileService;
import com.capstone.backend.service.iservice.IPostMediaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostMediaImpl implements IPostMediaService {

  private final PostMediaRepository postMediaRepository;

  private final IFileService fileService;

  @Override
  public PostMedia save(PostMedia media, MultipartFile file) {
    PostMedia initialMedia = postMediaRepository.save(media);
    final String folder = "post_medias";
    Optional<String> optionalUrl = fileService.store(folder, initialMedia.getId(), file);
    optionalUrl.ifPresent(url -> initialMedia.setUrl(url));
    return postMediaRepository.save(initialMedia);
  }

}
