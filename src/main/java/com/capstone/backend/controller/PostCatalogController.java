package com.capstone.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.backend.dto.catalog.PostCatalogRequest;
import com.capstone.backend.dto.catalog.PostCatalogResponse;
import com.capstone.backend.exception.ResourceAlreadyExists;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.service.iservice.IPostCatalogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post-catalog")
public class PostCatalogController {

  private final IPostCatalogService postCatalogService;

  @GetMapping(value = "/public")
  public ResponseEntity<?> getPostCatalogs() {
    List<PostCatalogResponse> catalogs = postCatalogService.getPostCatalogs();
    return new ResponseEntity<>(catalogs, HttpStatus.OK);
  }

  @PostMapping(value = "")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> createPostCatalog(@RequestBody PostCatalogRequest request)
      throws ResourceAlreadyExists {
    PostCatalogResponse response = postCatalogService.createPostCatalog(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping(value = "/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> updatePostCatalog(
      @PathVariable long id,
      @RequestBody PostCatalogRequest request)
      throws ResourceNotFoundException, ResourceAlreadyExists {
    PostCatalogResponse response = postCatalogService.updatePostCatalog(id, request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> deletePostCatalog(@PathVariable long id)
      throws ResourceNotFoundException {
    postCatalogService.deletePostCatalog(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
