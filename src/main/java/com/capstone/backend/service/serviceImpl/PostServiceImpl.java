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
import com.capstone.backend.dto.common.ListIdRequest;
import com.capstone.backend.dto.post.CreatePostRequest;
import com.capstone.backend.dto.post.DenyPostRequest;
import com.capstone.backend.dto.post.PostResponse;
import com.capstone.backend.dto.post.UpdatePostRequest;
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
  public Post findById(long id) throws ResourceNotFoundException {
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));
    if (post.isDeleted()) {
      throw new ResourceNotFoundException(
          messageSource.getMessage("error.resource-not-found", null, Locale.getDefault()));
    }
    return post;
  }

  @Override
  public Post save(Post post) {
    return postRepository.save(post);
  }

  @Override
  public List<PostResponse> getPosts() {
    return postRepository.findAll()
        .stream()
        .filter(post -> !post.isDeleted())
        .sorted(Comparator.comparingLong(Post::getId))
        .map(postResponseMapper)
        .collect(Collectors.toList());
  }

  @Override
  public List<PostResponse> getPublicPosts() {
    return postRepository.findAll()
        .stream().filter(post -> !post.isDeleted()
            && post.getStatus().equals(PostStatuses.PUBLIC)
            && post.getExpiredAt().isAfter(LocalDateTime.now()))
        .sorted(Comparator.comparingLong(Post::getId))
        .map(postResponseMapper)
        .collect(Collectors.toList());
  }

  @Override
  public List<PostResponse> getPersonalPosts(String email)
      throws ResourceNotFoundException {
    User user = userService.findByEmail(email);
    return user.getPosts()
        .stream()
        .filter(post -> !post.isDeleted())
        .sorted(Comparator.comparingLong(Post::getId))
        .map(postResponseMapper)
        .collect(Collectors.toList());
  }

  @Override
  public PostResponse getPost(long id) throws ResourceNotFoundException {
    return postResponseMapper.apply(findById(id));
  }

  @Override
  public PostResponse createPost(CreatePostRequest request, PostStatuses status)
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

    if (status.equals(PostStatuses.PUBLIC)) {
      post.setStatus(PostStatuses.PUBLIC);
      request.type().ifPresent(type -> post.setType(PostTypes.valueOf(type)));
      post.setPaidAt(LocalDateTime.now());
      request.expired_at()
          .ifPresent(time -> post
              .setExpiredAt(LocalDateTime.parse(time,
                  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    } else if (status.equals(PostStatuses.UNCONFIRMED)) {
      post.setStatus(PostStatuses.UNCONFIRMED);
    }

    Post savedPost = postRepository.save(post);
    Arrays.asList(request.images()).stream().forEach(file -> {
      PostMedia media = new PostMedia(savedPost);
      postMediaService.save(media, file);
    });

    return postResponseMapper.apply(postRepository.findById(savedPost.getId()).get());
  }

  @Override
  public PostResponse updatePost(long id, UpdatePostRequest request) throws ResourceNotFoundException {
    Post post = findById(id);

    post.setTitle(request.title());
    post.setDescription(request.description());
    post.setPrice(request.price());
    post.setDeposit(request.deposit());
    post.setTarget(PostTargets.valueOf(request.target()));
    request.type().ifPresent(type -> post.setType(PostTypes.valueOf(type)));
    post.setAcreage(request.acreage());
    request.expired_at()
        .ifPresent(time -> post
            .setExpiredAt(LocalDateTime.parse(time,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

    if (post.getStatus().equals(PostStatuses.DENIED)) {
      post.setStatus(PostStatuses.UNCONFIRMED);
    }

    PostCatalog catalog = postCatalogService.findById(request.catalog_id());
    post.setCatalog(catalog);

    Address address = post.getAddress();
    address.setProvince(request.province());
    address.setDistrict(request.district());
    address.setWard(request.ward());
    address.setSpecificAddress(request.specific_address());
    addressService.save(address);

    if (request.removed_media_ids().isPresent()) {
      for (long mediaId : request.removed_media_ids().get()) {
        PostMedia media = postMediaService.findById(mediaId);
        post.removeMedia(media);
      }
    }

    if (request.images().isPresent()) {
      Arrays.asList(request.images().get()).stream().forEach(file -> {
        PostMedia media = new PostMedia(post);
        postMediaService.save(media, file);
      });
    }

    return postResponseMapper.apply(postRepository.save(post));
  }

  @Override
  public void confirmPost(ListIdRequest ids)
      throws ResourceNotFoundException {
    for (long id : ids.ids()) {
      Post post = findById(id);

      post.setStatus(PostStatuses.UNPAID);
      postRepository.save(post);
    }
  }

  @Override
  public void denyPost(long id, DenyPostRequest request) throws ResourceNotFoundException {
    Post post = findById(id);

    post.setStatus(PostStatuses.DENIED);
    post.setRefusedReason(request.refusedReason());
    postRepository.save(post);
  }

  @Override
  public void deletePost(ListIdRequest ids) throws ResourceNotFoundException {
    for (long id : ids.ids()) {
      Post post = findById(id);

      post.setDeleted(true);
      postRepository.save(post);
    }
  }
}
