package com.capstone.backend.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.backend.dto.paymentMethod.PaymentMethodRequest;
import com.capstone.backend.dto.paymentMethod.PaymentMethodResponse;
import com.capstone.backend.service.iservice.IPaymentMethodService;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/payment-method")
public class PaymentMethodController {

  private final IPaymentMethodService paymentMethodService;

  @PostMapping(value = "/momo")
  public ResponseEntity<?> momoPayment(@RequestBody PaymentMethodRequest request) throws Exception {
    PaymentMethodResponse response = paymentMethodService.momoMethod(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "vnpay")
  public ResponseEntity<?> vnpayMethod(@RequestBody PaymentMethodRequest request) throws UnsupportedEncodingException {
    PaymentMethodResponse response = paymentMethodService.vnpayMethod(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/paypal")
  public ResponseEntity<?> paypalMethod(@RequestBody PaymentMethodRequest request)
      throws PayPalRESTException {
    PaymentMethodResponse response = paymentMethodService.paypalMethod(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
