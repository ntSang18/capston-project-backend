package com.capstone.backend.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.capstone.backend.dto.promotion.PromotionChildDto;
import com.capstone.backend.dto.promotion.PromotionRequest;
import com.capstone.backend.dto.promotion.PromotionResponse;
import com.capstone.backend.exception.ConflictTimeException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.mapper.PromotionResponseMapper;
import com.capstone.backend.model.Promotion;
import com.capstone.backend.model.PromotionChild;
import com.capstone.backend.repository.PromotionRepository;
import com.capstone.backend.service.iservice.IPromotionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements IPromotionService {

  private final PromotionRepository promotionRepository;

  private final MessageSource messageSource;

  private final PromotionResponseMapper promotionResponseMapper;

  @Override
  public Promotion findById(long id) throws ResourceNotFoundException {
    Promotion promotion = promotionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
        messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));

    if (promotion.isDeleted()) {
      throw new ResourceNotFoundException(
          messageSource.getMessage("error.resource-not-found", null, Locale.getDefault()));
    }

    return promotion;
  }

  @Override
  public Optional<Promotion> getCurrentPromotion() {
    List<Promotion> promotions = promotionRepository
        .findAll()
        .stream()
        .filter(p -> !p.isDeleted()).collect(Collectors.toList());

    LocalDateTime now = LocalDateTime.now();

    for (Promotion promotion : promotions) {
      if (promotion.getStartAt().isBefore(now) && promotion.getEndAt().isAfter(now)) {
        return Optional.of(promotion);
      }
    }
    return Optional.empty();
  }

  @Override
  public List<PromotionResponse> getPromotions() {
    return promotionRepository
        .findAll()
        .stream()
        .filter(promotion -> !promotion.isDeleted())
        .sorted(Comparator.comparing(Promotion::getEndAt))
        .map(promotionResponseMapper).collect(Collectors.toList());
  }

  @Override
  public PromotionResponse createPromotion(PromotionRequest request)
      throws ConflictTimeException {

    if (isConflictTime(0, request.startAt(), request.endAt())) {
      throw new ConflictTimeException(
          messageSource.getMessage("error.conflict-time", null, Locale.getDefault()));
    }

    Promotion promotion = new Promotion(request.name(), request.startAt(), request.endAt());

    for (PromotionChildDto childDto : request.childs()) {
      PromotionChild child = new PromotionChild(childDto.startRange(), childDto.percent(), promotion);
      if (childDto.endRange().isPresent()) {
        child.setEndRange(childDto.endRange().get());
      } else {
        child.setEndRange(999999999);
      }
      promotion.addChild(child);
    }

    return promotionResponseMapper.apply(promotionRepository.save(promotion));
  }

  @Override
  public PromotionResponse updatePromotion(long id, PromotionRequest request)
      throws ConflictTimeException, ResourceNotFoundException {
    if (isConflictTime(id, request.startAt(), request.endAt())) {
      throw new ConflictTimeException(
          messageSource.getMessage("error.conflict-time", null, Locale.getDefault()));
    }

    Promotion promotion = findById(id);
    promotion.setName(request.name());
    promotion.setStartAt(request.startAt());
    promotion.setEndAt(request.endAt());
    promotion.removeAllChild();

    for (PromotionChildDto childDto : request.childs()) {
      PromotionChild child = new PromotionChild(childDto.startRange(), childDto.percent(), promotion);
      if (childDto.endRange().isPresent()) {
        child.setEndRange(childDto.endRange().get());
      } else {
        child.setEndRange(999999999);
      }
      promotion.addChild(child);
    }

    return promotionResponseMapper.apply(promotionRepository.save(promotion));
  }

  @Override
  public void deletePromotion(long id) throws ResourceNotFoundException {
    Promotion promotion = findById(id);
    promotion.setDeleted(true);
    promotionRepository.save(promotion);
  }

  private boolean isConflictTime(long id, LocalDateTime start, LocalDateTime end) {
    List<PromotionResponse> promotions = getPromotions();
    for (PromotionResponse promotion : promotions) {
      if (promotion.id() == id) {
        continue;
      }
      if ((start.isAfter(promotion.startAt())
          && start.isBefore(promotion.endAt()))
          || (end.isAfter(promotion.startAt())
              && end.isBefore(promotion.endAt()))
          || (start.isBefore(promotion.startAt())
              && end.isAfter(promotion.endAt()))) {
        return true;
      }
    }
    return false;
  }
}
