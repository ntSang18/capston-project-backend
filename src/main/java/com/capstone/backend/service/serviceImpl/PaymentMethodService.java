package com.capstone.backend.service.serviceImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.capstone.backend.config.VNPayConfig;
import com.capstone.backend.dto.paymentMethod.PaymentMethodRequest;
import com.capstone.backend.dto.paymentMethod.PaymentMethodResponse;
import com.capstone.backend.service.iservice.IPaymentMethodService;
import com.mservice.allinone.models.CaptureMoMoResponse;
import com.mservice.allinone.processor.allinone.CaptureMoMo;
import com.mservice.shared.sharedmodels.Environment;
import com.mservice.shared.sharedmodels.Environment.ProcessType;

@Service
public class PaymentMethodService implements IPaymentMethodService {

  @Value("${application.frontend.default-url}")
  private String frontendUrl;
  public String vnp_Returnurl = frontendUrl + "/transaction/vnpay";

  @Override
  public PaymentMethodResponse vnpayMethod(PaymentMethodRequest request) throws UnsupportedEncodingException {
    long amount = request.amount() * 100;
    String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
    String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

    Map<String, String> vnp_Params = new HashMap<>();
    vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
    vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
    vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
    vnp_Params.put("vnp_Amount", String.valueOf(amount));
    vnp_Params.put("vnp_CurrCode", "VND");
    vnp_Params.put("vnp_BankCode", "NCB");
    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
    vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
    vnp_Params.put("vnp_Locale", "vn");
    vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);

    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String vnp_CreateDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

    cld.add(Calendar.MINUTE, 15);
    String vnp_ExpireDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

    List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
    Collections.sort(fieldNames);
    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();
    Iterator<String> itr = fieldNames.iterator();
    while (itr.hasNext()) {
      String fieldName = (String) itr.next();
      String fieldValue = (String) vnp_Params.get(fieldName);
      if ((fieldValue != null) && (fieldValue.length() > 0)) {
        hashData.append(fieldName);
        hashData.append('=');
        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
        query.append('=');
        query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
        if (itr.hasNext()) {
          query.append('&');
          hashData.append('&');
        }
      }
    }
    String queryUrl = query.toString();
    String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
    queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
    String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

    return new PaymentMethodResponse(paymentUrl);
  }

  @Override
  public PaymentMethodResponse momoMethod(PaymentMethodRequest request) throws Exception {
    String requestId = String.valueOf(System.currentTimeMillis());
    String orderId = String.valueOf(System.currentTimeMillis());
    long amount = 50000; // request amount
    String orderInfo = "Pay With MoMo";
    String returnURL = "https://google.com.vn";
    String notifyURL = "https://google.com.vn";

    Environment environment = Environment.selectEnv("dev", ProcessType.PAY_GATE);
    CaptureMoMoResponse captureMoMoResponse = CaptureMoMo.process(
        environment,
        orderId,
        requestId,
        Long.toString(amount),
        orderInfo,
        returnURL,
        notifyURL,
        "");

    return new PaymentMethodResponse(captureMoMoResponse.getPayUrl());
  }

}
