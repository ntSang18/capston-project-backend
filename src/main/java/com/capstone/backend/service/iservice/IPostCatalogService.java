package com.capstone.backend.service.iservice;

import java.util.List;

import com.capstone.backend.dto.catalog.PostCatalogRequest;
import com.capstone.backend.dto.catalog.PostCatalogResponse;
import com.capstone.backend.exception.ResourceAlreadyExists;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.PostCatalog;

public interface IPostCatalogService {

  PostCatalog findById(long id) throws ResourceNotFoundException;

  List<PostCatalogResponse> getPostCatalogs();

  PostCatalogResponse createPostCatalog(PostCatalogRequest request) throws ResourceAlreadyExists;

  PostCatalogResponse updatePostCatalog(long id, PostCatalogRequest request)
      throws ResourceNotFoundException, ResourceAlreadyExists;

  void deletePostCatalog(long id) throws ResourceNotFoundException;
}
