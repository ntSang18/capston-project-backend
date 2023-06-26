package com.capstone.backend.service.iservice;

import java.util.List;

import com.capstone.backend.dto.transaction.PaypalExecuteRequest;
import com.capstone.backend.dto.transaction.TransactionRequest;
import com.capstone.backend.dto.transaction.TransactionResponse;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.Transaction;
import com.paypal.base.rest.PayPalRESTException;

public interface ITransactionService {

  Transaction findById(long id) throws ResourceNotFoundException;

  Transaction save(Transaction transaction);

  List<TransactionResponse> getTransactions();

  List<TransactionResponse> getPersonalTransactions(String email) throws ResourceNotFoundException;

  TransactionResponse getTransaction(long id) throws ResourceNotFoundException;

  TransactionResponse createTransaction(String email, TransactionRequest request)
      throws ResourceNotFoundException;

  TransactionResponse executePaypal(String email, PaypalExecuteRequest request)
      throws PayPalRESTException, ResourceNotFoundException;

  void deleteTransaction(long id) throws ResourceNotFoundException;

}
