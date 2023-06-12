package com.capstone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.backend.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
