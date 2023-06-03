package com.capstone.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.backend.constant.PostStatuses;
import com.capstone.backend.dto.common.ListIdRequest;
import com.capstone.backend.dto.post.CreatePostRequest;
import com.capstone.backend.dto.post.DenyPostRequest;
import com.capstone.backend.dto.post.PostResponse;
import com.capstone.backend.dto.post.UpdatePostRequest;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.service.iservice.IPostService;
import com.capstone.backend.model.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/post")
public class PostController {

  private final IPostService postService;

  @GetMapping(value = "")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> getPosts() {
    List<PostResponse> response = postService.getPosts();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = "/public")
  public ResponseEntity<?> getPublicPosts() {
    List<PostResponse> response = postService.getPublicPosts();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = "/personal")
  public ResponseEntity<?> getPersonalPosts(@AuthenticationPrincipal UserDetailsImpl user)
      throws ResourceNotFoundException {
    List<PostResponse> response = postService.getPersonalPosts(user.getUsername());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = "/public/{id}")
  public ResponseEntity<?> getPost(@PathVariable("id") long id)
      throws ResourceNotFoundException {
    PostResponse response = postService.getPost(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/create-public", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> createPublicPost(@ModelAttribute CreatePostRequest request)
      throws ResourceNotFoundException {
    PostResponse response = postService.createPost(request, PostStatuses.PUBLIC);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<?> createPost(@ModelAttribute CreatePostRequest request)
      throws ResourceNotFoundException {
    PostResponse response = postService.createPost(request, PostStatuses.UNCONFIRMED);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<?> updatePost(@PathVariable long id, @ModelAttribute UpdatePostRequest request)
      throws ResourceNotFoundException {
    PostResponse response = postService.updatePost(id, request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping(value = "/confirm")
  public ResponseEntity<?> confirmPost(@RequestBody ListIdRequest ids)
      throws ResourceNotFoundException {
    postService.confirmPost(ids);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PatchMapping(value = "/deny/{id}")
  public ResponseEntity<?> denyPost(@PathVariable long id, @RequestBody DenyPostRequest request)
      throws ResourceNotFoundException {
    postService.denyPost(id, request);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<?> deletePost(@RequestBody ListIdRequest ids)
      throws ResourceNotFoundException {
    postService.deletePost(ids);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
