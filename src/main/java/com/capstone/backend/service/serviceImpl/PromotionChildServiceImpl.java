package com.capstone.backend.service.serviceImpl;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.PromotionChild;
import com.capstone.backend.repository.PromotionChildRepository;
import com.capstone.backend.service.iservice.IPromotionChildService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionChildServiceImpl implements IPromotionChildService {

  private final PromotionChildRepository promotionChildRepository;

  private final MessageSource messageSource;

  @Override
  public PromotionChild findById(long id) throws ResourceNotFoundException {
    return promotionChildRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
        messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));
  }

  @Override
  public PromotionChild save(PromotionChild child) {
    return promotionChildRepository.save(child);
  }

  @Override
  public void delete(long id) {
    promotionChildRepository.deleteById(id);
  }

}
