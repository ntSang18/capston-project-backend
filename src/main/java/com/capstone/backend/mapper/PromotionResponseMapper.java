package com.capstone.backend.mapper;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.promotion.PromotionResponse;
import com.capstone.backend.model.Promotion;
import com.capstone.backend.model.PromotionChild;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionResponseMapper implements Function<Promotion, PromotionResponse> {

  private final PromotionChildDtoMapper promotionChildDtoMapper;

  @Override
  public PromotionResponse apply(Promotion promotion) {
    return new PromotionResponse(
        promotion.getId(),
        promotion.getName(),
        promotion.getStartAt(),
        promotion.getEndAt(),
        promotion.getChilds()
            .stream()
            .sorted(Comparator.comparingInt(PromotionChild::getPercent))
            .map(promotionChildDtoMapper).collect(Collectors.toList()));
  }

}
