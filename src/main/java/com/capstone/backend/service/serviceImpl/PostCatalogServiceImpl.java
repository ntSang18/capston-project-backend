package com.capstone.backend.service.serviceImpl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.capstone.backend.dto.catalog.PostCatalogRequest;
import com.capstone.backend.dto.catalog.PostCatalogResponse;
import com.capstone.backend.exception.ResourceAlreadyExists;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.mapper.PostCatalogResponseMapper;
import com.capstone.backend.model.PostCatalog;
import com.capstone.backend.repository.PostCatalogRepository;
import com.capstone.backend.service.iservice.IPostCatalogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostCatalogServiceImpl implements IPostCatalogService {

  private final PostCatalogRepository postCatalogRepository;

  private final PostCatalogResponseMapper postCatalogResponseMapper;

  private final MessageSource messageSource;

  @Override
  public PostCatalog findById(long id) throws ResourceNotFoundException {
    PostCatalog catalog = postCatalogRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));

    if (catalog.isDeleted()) {
      throw new ResourceNotFoundException(
          messageSource.getMessage("error.resource-not-found", null, Locale.getDefault()));
    }

    return catalog;

  }

  @Override
  public List<PostCatalogResponse> getPostCatalogs() {
    return postCatalogRepository
        .findAll()
        .stream()
        .filter(catalog -> !catalog.isDeleted())
        .map(postCatalogResponseMapper)
        .collect(Collectors.toList());
  }

  @Override
  public PostCatalogResponse createPostCatalog(PostCatalogRequest request)
      throws ResourceAlreadyExists {
    if (postCatalogRepository.findByName(request.name()).isPresent()) {
      throw new ResourceAlreadyExists(
          messageSource.getMessage("error.resource-already-exists", null, Locale.getDefault()));
    }
    PostCatalog catalog = postCatalogRepository.save(new PostCatalog(request.name()));
    return postCatalogResponseMapper.apply(catalog);
  }

  @Override
  public PostCatalogResponse updatePostCatalog(long id, PostCatalogRequest request)
      throws ResourceNotFoundException, ResourceAlreadyExists {
    PostCatalog catalog = findById(id);

    Optional<PostCatalog> optionalCheckCatalog = postCatalogRepository.findByName(request.name());
    if (optionalCheckCatalog.isPresent() && optionalCheckCatalog.get().getId() != catalog.getId()) {
      throw new ResourceAlreadyExists(
          messageSource.getMessage("error.resource-already-exists", null, Locale.getDefault()));
    }
    catalog.setName(request.name());
    PostCatalog updatedCatalog = postCatalogRepository.save(catalog);

    return postCatalogResponseMapper.apply(updatedCatalog);
  }

  @Override
  public void deletePostCatalog(long id) throws ResourceNotFoundException {
    PostCatalog catalog = findById(id);
    catalog.setDeleted(true);
    postCatalogRepository.save(catalog);
  }

}
