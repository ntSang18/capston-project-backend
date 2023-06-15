package com.capstone.backend.service.iservice;

import java.util.List;

import com.capstone.backend.dto.postPayment.PostPaymentRequest;
import com.capstone.backend.dto.postPayment.PostPaymentResponse;
import com.capstone.backend.exception.DifferentTypeException;
import com.capstone.backend.exception.InsufficientBalanceException;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.exception.UnqualifiedException;
import com.capstone.backend.model.PostPayment;

public interface IPostPaymentService {

  PostPayment findById(long id) throws ResourceNotFoundException;

  List<PostPaymentResponse> getPostPayments();

  List<PostPaymentResponse> getPersonalPostPayments(String email);

  PostPaymentResponse getPostPayment(long id) throws ResourceNotFoundException;

  PostPaymentResponse purchase(String email, PostPaymentRequest request)
      throws ResourceNotFoundException, InsufficientBalanceException, DifferentTypeException, UnqualifiedException;

  void deletePostPayment(long id) throws ResourceNotFoundException;

}
