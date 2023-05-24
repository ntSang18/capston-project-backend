package com.capstone.backend.mapper;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.post.PostResponse;
import com.capstone.backend.model.Post;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostResponseMapper implements Function<Post, PostResponse> {

  private final AddressDtoMapper addressDtoMapper;

  private final UserResponseMapper userResponseMapper;

  @Override
  public PostResponse apply(Post post) {
    return new PostResponse(
        post.getId(),
        post.getTitle(),
        post.getDescription(),
        post.getPrice(),
        post.getDeposit(),
        post.getTarget().name(),
        post.getAcreage(),
        post.getType().name(),
        post.getStatus().name(),
        post.getCreatedAt(),
        post.getConfirmedAt(),
        post.getDeclinedAt(),
        post.getPaidAt(),
        post.getExpiredAt(),
        userResponseMapper.apply(post.getUser()),
        addressDtoMapper.apply(post.getAddress()),
        post.getMedias().stream().map(media -> media.getUrl()).collect(Collectors.toList()));
  }

}
