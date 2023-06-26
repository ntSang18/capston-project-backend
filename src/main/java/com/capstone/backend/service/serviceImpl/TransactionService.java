package com.capstone.backend.service.serviceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.capstone.backend.constant.PaymentMethods;
import com.capstone.backend.dto.transaction.PaypalExecuteRequest;
import com.capstone.backend.dto.transaction.TransactionRequest;
import com.capstone.backend.dto.transaction.TransactionResponse;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.mapper.TransactionResponseMapper;
import com.capstone.backend.model.Promotion;
import com.capstone.backend.model.PromotionChild;
import com.capstone.backend.model.Transaction;
import com.capstone.backend.model.User;
import com.capstone.backend.repository.TransactionRepository;
import com.capstone.backend.service.iservice.IPromotionService;
import com.capstone.backend.service.iservice.ITransactionService;
import com.capstone.backend.service.iservice.IUserService;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

  private final TransactionRepository transactionRepository;

  private final IUserService userService;

  private final TransactionResponseMapper transactionResponseMapper;

  private final IPromotionService promotionService;

  private final MessageSource messageSource;

  private final APIContext apiContext;

  @Override
  public Transaction findById(long id) throws ResourceNotFoundException {
    Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
        messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));

    if (transaction.isDeleted()) {
      throw new ResourceNotFoundException(
          messageSource.getMessage("error.resource-not-found", null, Locale.getDefault()));
    }

    return transaction;
  }

  @Override
  public Transaction save(Transaction transaction) {
    return transactionRepository.save(transaction);
  }

  @Override
  public List<TransactionResponse> getTransactions() {
    return transactionRepository.findAll()
        .stream()
        .filter(trans -> !trans.isDeleted())
        .sorted(Comparator.comparingLong(Transaction::getId))
        .map(transactionResponseMapper)
        .collect(Collectors.toList());
  }

  @Override
  public List<TransactionResponse> getPersonalTransactions(String email)
      throws ResourceNotFoundException {
    User user = userService.findByEmail(email);
    return user.getTransactions()
        .stream()
        .filter(trans -> !trans.isDeleted())
        .sorted(Comparator.comparingLong(Transaction::getId))
        .map(transactionResponseMapper)
        .collect(Collectors.toList());

  }

  @Override
  public TransactionResponse getTransaction(long id) throws ResourceNotFoundException {
    return transactionResponseMapper.apply(findById(id));
  }

  @Override
  public TransactionResponse createTransaction(String email, TransactionRequest request)
      throws ResourceNotFoundException {
    User user = userService.findByEmail(email);
    long actualMoney = 0;
    double discount = 0;

    Optional<Promotion> optionalPromotion = promotionService.getCurrentPromotion();
    boolean isDiscounted = false;
    if (optionalPromotion.isPresent()) {
      for (PromotionChild child : optionalPromotion.get().getChilds()) {
        if (request.money() >= child.getStartRange() && request.money() < child.getEndRange()) {
          discount = (double) child.getPercent() / 100;
          actualMoney = (long) (request.money() * (1 + discount));
          user.setBalance(user.getBalance() + actualMoney);
          isDiscounted = true;
          break;
        }
      }
    }
    if (!isDiscounted) {
      actualMoney = request.money();
      user.setBalance(user.getBalance() + actualMoney);
    }
    userService.save(user);
    Transaction transaction = new Transaction(
        request.money(),
        actualMoney,
        PaymentMethods.valueOf(request.method()),
        discount,
        user);

    return transactionResponseMapper.apply(transactionRepository.save(transaction));
  }

  @Override
  public TransactionResponse executePaypal(String email, PaypalExecuteRequest request)
      throws PayPalRESTException, ResourceNotFoundException {
    Payment payment = new Payment();
    payment.setId(request.paymentId());
    PaymentExecution paymentExecute = new PaymentExecution();
    paymentExecute.setPayerId(request.payerId());
    Payment resultPayment = payment.execute(apiContext, paymentExecute);
    if (resultPayment.getState().equals("approved")) {
      for (com.paypal.api.payments.Transaction transaction : resultPayment.getTransactions()) {
        long money = (long) (Double.parseDouble(transaction.getAmount().getTotal()) * 23000);
        String method = PaymentMethods.PAYPAL.name();
        TransactionRequest req = new TransactionRequest(money, method);
        return createTransaction(email, req);
      }
    }
    return null;
  }

  @Override
  public void deleteTransaction(long id) throws ResourceNotFoundException {
    Transaction transaction = findById(id);
    transaction.setDeleted(true);
    transactionRepository.save(transaction);
  }

}
