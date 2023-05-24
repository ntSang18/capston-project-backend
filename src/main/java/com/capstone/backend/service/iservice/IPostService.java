package com.capstone.backend.service.iservice;

import java.util.List;

import com.capstone.backend.dto.post.CreatePublicPostRequest;
import com.capstone.backend.dto.post.PostResponse;
import com.capstone.backend.exception.ResourceNotFoundException;

public interface IPostService {
  List<PostResponse> getPosts();

  PostResponse getPost(long id) throws ResourceNotFoundException;

  PostResponse createPublicPost(CreatePublicPostRequest request) throws ResourceNotFoundException;
}
