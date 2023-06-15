package com.capstone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.backend.model.PostPayment;

public interface PostPaymentRepository extends JpaRepository<PostPayment, Long> {

}
