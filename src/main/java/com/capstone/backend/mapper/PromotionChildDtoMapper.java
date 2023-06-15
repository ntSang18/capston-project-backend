package com.capstone.backend.mapper;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.promotion.PromotionChildDto;
import com.capstone.backend.model.PromotionChild;

@Service
public class PromotionChildDtoMapper implements Function<PromotionChild, PromotionChildDto> {

  @Override
  public PromotionChildDto apply(PromotionChild promotionChild) {
    return new PromotionChildDto(
        Optional.of(promotionChild.getId()),
        promotionChild.getStartRange(),
        Optional.of(promotionChild.getEndRange()),
        promotionChild.getPercent());
  }

}
