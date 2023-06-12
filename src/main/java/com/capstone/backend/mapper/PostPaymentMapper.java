package com.capstone.backend.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.postPayment.PostPaymentResponse;
import com.capstone.backend.model.PostPayment;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostPaymentMapper implements Function<PostPayment, PostPaymentResponse> {

  private final PostResponseMapper postResponseMapper;

  private final PackagePriceResponseMapper packagePriceResponseMapper;

  @Override
  public PostPaymentResponse apply(PostPayment postPayment) {
    return new PostPaymentResponse(
        postPayment.getId(),
        postPayment.getNumberPackage(),
        postPayment.getPaidAt(),
        postResponseMapper.apply(postPayment.getPost()),
        packagePriceResponseMapper.apply(postPayment.getPackagePrice()));
  }

}
