package com.capstone.backend.service.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.capstone.backend.constant.PostStatuses;
import com.capstone.backend.constant.PostTargets;
import com.capstone.backend.constant.PostTypes;
import com.capstone.backend.dto.post.CreatePublicPostRequest;
import com.capstone.backend.dto.post.PostResponse;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.mapper.PostResponseMapper;
import com.capstone.backend.model.Address;
import com.capstone.backend.model.Post;
import com.capstone.backend.model.PostCatalog;
import com.capstone.backend.model.PostMedia;
import com.capstone.backend.model.User;
import com.capstone.backend.repository.PostRepository;
import com.capstone.backend.service.iservice.IAddressService;
import com.capstone.backend.service.iservice.IPostCatalogService;
import com.capstone.backend.service.iservice.IPostMediaService;
import com.capstone.backend.service.iservice.IPostService;
import com.capstone.backend.service.iservice.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements IPostService {

  private final PostRepository postRepository;

  private final IUserService userService;

  private final IAddressService addressService;

  private final IPostCatalogService postCatalogService;

  private final IPostMediaService postMediaService;

  private final PostResponseMapper postResponseMapper;

  private final MessageSource messageSource;

  @Override
  public List<PostResponse> getPosts() {
    return postRepository.findAll()
        .stream().filter(post -> !post.isDeleted())
        .sorted(Comparator.comparingLong(Post::getId))
        .map(postResponseMapper)
        .collect(Collectors.toList());
  }

  @Override
  public PostResponse getPost(long id) throws ResourceNotFoundException {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));

    if (post.isDeleted()) {
      throw new ResourceNotFoundException(
          messageSource.getMessage("error.resource-not-found", null, Locale.getDefault()));
    }

    return postResponseMapper.apply(post);
  }

  @Override
  public PostResponse createPublicPost(CreatePublicPostRequest request)
      throws ResourceNotFoundException {
    User userCreate = userService.findById(request.user_id());
    Address savedAddress = addressService.save(new Address(
        request.province(),
        request.district(),
        request.ward(),
        request.specific_address()));
    PostCatalog catalog = postCatalogService.findById(request.catalog_id());

    Post post = new Post(
        request.title(),
        request.description(),
        request.price(),
        request.deposit(),
        PostTargets.valueOf(request.target()),
        request.acreage(),
        catalog,
        userCreate,
        savedAddress);
    post.setStatus(PostStatuses.PUBLIC);
    post.setType(PostTypes.valueOf(request.type()));
    post.setExpiredAt(
      LocalDateTime.parse(request.expired_at(), 
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    Post savedPost = postRepository.save(post);
    Arrays.asList(request.images()).stream().forEach(file -> {
      PostMedia media = new PostMedia(savedPost);
      postMediaService.save(media, file);
    });

    return postResponseMapper.apply(postRepository.findById(savedPost.getId()).get());
  }

}
