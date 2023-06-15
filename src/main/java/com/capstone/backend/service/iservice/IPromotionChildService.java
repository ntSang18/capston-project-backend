package com.capstone.backend.service.iservice;

import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.PromotionChild;

public interface IPromotionChildService {

  PromotionChild findById(long id) throws ResourceNotFoundException;

  PromotionChild save(PromotionChild child);

  void delete(long id);

}
