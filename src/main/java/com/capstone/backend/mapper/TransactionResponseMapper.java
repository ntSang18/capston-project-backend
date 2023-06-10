package com.capstone.backend.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.transaction.TransactionResponse;
import com.capstone.backend.model.Transaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionResponseMapper implements Function<Transaction, TransactionResponse> {

  private final UserResponseMapper userResponseMapper;

  @Override
  public TransactionResponse apply(Transaction trans) {
    return new TransactionResponse(
        trans.getId(),
        trans.getCreatedAt(),
        trans.getMoney(),
        trans.getActualMoney(),
        trans.getMethod().name(),
        trans.getDiscount(),
        userResponseMapper.apply(trans.getUser()));
  }

}
