package com.capstone.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.backend.dto.promotion.PromotionRequest;
import com.capstone.backend.dto.promotion.PromotionResponse;
import com.capstone.backend.exception.ConflictTimeException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.mapper.PromotionResponseMapper;
import com.capstone.backend.model.Promotion;
import com.capstone.backend.service.iservice.IPromotionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/promotion")
public class PromotionController {

  private final IPromotionService promotionService;

  private final PromotionResponseMapper promotionResponseMapper;

  @GetMapping(value = "/public/current")
  public ResponseEntity<?> getCurrentPromotion() {
    Optional<Promotion> optional = promotionService.getCurrentPromotion();
    PromotionResponse res;
    if (optional.isPresent()) {
      res = promotionResponseMapper.apply(optional.get());
    } else {
      res = null;
    }

    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getPromotions() {
    List<PromotionResponse> res = promotionService.getPromotions();

    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PostMapping(value = "")
  public ResponseEntity<?> createPromotion(@RequestBody PromotionRequest request)
      throws ConflictTimeException {
    PromotionResponse res = promotionService.createPromotion(request);

    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PatchMapping(value = "/{id}")
  public ResponseEntity<?> updatePromotion(@PathVariable long id, @RequestBody PromotionRequest request)
      throws ConflictTimeException, ResourceNotFoundException {
    PromotionResponse res = promotionService.updatePromotion(id, request);

    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deletePromotion(@PathVariable long id)
      throws ResourceNotFoundException {
    promotionService.deletePromotion(id);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
