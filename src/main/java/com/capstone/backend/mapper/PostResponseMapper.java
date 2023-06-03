package com.capstone.backend.mapper;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.post.PostResponse;
import com.capstone.backend.model.Post;
import com.capstone.backend.model.PostMedia;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostResponseMapper implements Function<Post, PostResponse> {

  private final AddressDtoMapper addressDtoMapper;

  private final UserResponseMapper userResponseMapper;

  private final PostCatalogDtoMapper postCatalogDtoMapper;

  private final PostMediaDtoMapper postMediaDtoMapper;

  @Override
  public PostResponse apply(Post post) {
    String type = post.getType() != null ? post.getType().name() : null;
    return new PostResponse(
        post.getId(),
        post.getTitle(),
        post.getDescription(),
        post.getPrice(),
        post.getDeposit(),
        post.getTarget().name(),
        post.getAcreage(),
        type,
        post.getStatus().name(),
        post.getRefusedReason(),
        post.getCreatedAt(),
        post.getConfirmedAt(),
        post.getDeclinedAt(),
        post.getPaidAt(),
        post.getExpiredAt(),
        userResponseMapper.apply(post.getUser()),
        addressDtoMapper.apply(post.getAddress()),
        postCatalogDtoMapper.apply(post.getCatalog()),
        post.getMedias()
            .stream()
            .sorted(Comparator.comparingLong(PostMedia::getId))
            .map(postMediaDtoMapper)
            .collect(Collectors.toList()));
  }

}
