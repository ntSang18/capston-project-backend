package com.capstone.backend.service.iservice;

import java.util.List;

import com.capstone.backend.dto.common.ListIdRequest;
import com.capstone.backend.dto.post.CreatePublicPostRequest;
import com.capstone.backend.dto.post.DenyPostRequest;
import com.capstone.backend.dto.post.PostResponse;
import com.capstone.backend.dto.post.UpdatePostRequest;
import com.capstone.backend.exception.ResourceNotFoundException;

public interface IPostService {
  List<PostResponse> getPosts();

  PostResponse getPost(long id) throws ResourceNotFoundException;

  PostResponse createPublicPost(CreatePublicPostRequest request) throws ResourceNotFoundException;

  PostResponse updatePost(long id, UpdatePostRequest request) throws ResourceNotFoundException;

  void confirmPost(ListIdRequest ids) throws ResourceNotFoundException;

  void denyPost(long id, DenyPostRequest request) throws ResourceNotFoundException;

  void deletePost(ListIdRequest ids) throws ResourceNotFoundException;
}
