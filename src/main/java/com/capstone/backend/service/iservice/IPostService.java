package com.capstone.backend.service.iservice;

import java.util.List;

import com.capstone.backend.constant.PostStatuses;
import com.capstone.backend.dto.common.ListIdRequest;
import com.capstone.backend.dto.post.CreatePostRequest;
import com.capstone.backend.dto.post.DenyPostRequest;
import com.capstone.backend.dto.post.PostResponse;
import com.capstone.backend.dto.post.UpdatePostRequest;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.Post;

public interface IPostService {

  Post findById(long id) throws ResourceNotFoundException;

  Post save(Post post);

  List<PostResponse> getPosts();

  List<PostResponse> getPublicPosts();

  List<PostResponse> getPersonalPosts(String email) throws ResourceNotFoundException;

  PostResponse getPost(long id) throws ResourceNotFoundException;

  PostResponse createPost(CreatePostRequest request, PostStatuses status)
      throws ResourceNotFoundException;

  PostResponse updatePost(long id, UpdatePostRequest request) throws ResourceNotFoundException;

  void confirmPost(ListIdRequest ids) throws ResourceNotFoundException;

  void denyPost(long id, DenyPostRequest request) throws ResourceNotFoundException;

  void deletePost(ListIdRequest ids) throws ResourceNotFoundException;
}
