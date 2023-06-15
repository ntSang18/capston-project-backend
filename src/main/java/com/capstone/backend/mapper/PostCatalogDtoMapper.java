package com.capstone.backend.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.catalog.PostCatalogDto;
import com.capstone.backend.model.PostCatalog;

@Service
public class PostCatalogDtoMapper implements Function<PostCatalog, PostCatalogDto> {

  @Override
  public PostCatalogDto apply(PostCatalog catalog) {
    return new PostCatalogDto(catalog.getId(), catalog.getName());
  }

}
