package com.capstone.backend.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.media.PostMediaDto;
import com.capstone.backend.model.PostMedia;

@Service
public class PostMediaDtoMapper implements Function<PostMedia, PostMediaDto> {

  @Override
  public PostMediaDto apply(PostMedia media) {
    return new PostMediaDto(media.getId(), media.getUrl());
  }

}
