package com.capstone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.backend.model.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

}
