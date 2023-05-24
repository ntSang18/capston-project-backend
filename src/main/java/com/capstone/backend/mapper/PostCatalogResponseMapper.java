package com.capstone.backend.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.constant.PostStatuses;
import com.capstone.backend.dto.catalog.PostCatalogResponse;
import com.capstone.backend.model.PostCatalog;

@Service
public class PostCatalogResponseMapper implements Function<PostCatalog, PostCatalogResponse> {

  @Override
  public PostCatalogResponse apply(PostCatalog catalog) {
    return new PostCatalogResponse(
        catalog.getId(),
        catalog.getName(),
        catalog.getCreatedAt(),
        catalog.getPosts().size(),
        catalog.getPosts().stream().filter(post -> post.getStatus().equals(PostStatuses.PUBLIC)).count(),
        catalog.getPosts().stream().filter(post -> post.getStatus().equals(PostStatuses.EXPiRED)).count(),
        catalog.getPosts().stream().filter(post -> post.getStatus().equals(PostStatuses.DENIED)).count(),
        catalog.getPosts().stream().filter(post -> post.getStatus().equals(PostStatuses.UNPAID)).count(),
        catalog.getPosts().stream().filter(post -> post.getStatus().equals(PostStatuses.UNCONFIRMED)).count());
  }

}
