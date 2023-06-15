package com.capstone.backend.service.iservice;

import java.util.List;
import java.util.Optional;

import com.capstone.backend.dto.promotion.PromotionRequest;
import com.capstone.backend.dto.promotion.PromotionResponse;
import com.capstone.backend.exception.ConflictTimeException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.Promotion;

public interface IPromotionService {
  Promotion findById(long id) throws ResourceNotFoundException;

  Optional<Promotion> getCurrentPromotion();

  List<PromotionResponse> getPromotions();

  PromotionResponse createPromotion(PromotionRequest request) throws ConflictTimeException;

  PromotionResponse updatePromotion(long id, PromotionRequest request)
      throws ConflictTimeException, ResourceNotFoundException;

  void deletePromotion(long id) throws ResourceNotFoundException;
}
