package com.capstone.backend.service.iservice;

import java.io.UnsupportedEncodingException;

import com.capstone.backend.dto.paymentMethod.PaymentMethodResponse;
import com.capstone.backend.dto.paymentMethod.PaymentMethodRequest;

public interface IPaymentMethodService {

  PaymentMethodResponse vnpayMethod(PaymentMethodRequest request) throws UnsupportedEncodingException;

  PaymentMethodResponse momoMethod(PaymentMethodRequest request) throws Exception;

}
