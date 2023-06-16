package com.capstone.backend.service.serviceImpl;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.PostMedia;
import com.capstone.backend.repository.PostMediaRepository;
import com.capstone.backend.service.iservice.IFileService;
import com.capstone.backend.service.iservice.IPostMediaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostMediaImpl implements IPostMediaService {

  private final PostMediaRepository postMediaRepository;

  private final MessageSource messageSource;

  private final IFileService fileService;

  @Override
  public PostMedia save(PostMedia media, MultipartFile file) {
    PostMedia initialMedia = postMediaRepository.save(media);
    final String folder = "post_medias";
    Optional<String> optionalUrl = fileService.store(folder, initialMedia.getId(), file);
    optionalUrl.ifPresent(url -> initialMedia.setUrl(url));
    return postMediaRepository.save(initialMedia);
  }

  @Override
  public PostMedia findById(long id) throws ResourceNotFoundException {
    return postMediaRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));
  }

}
