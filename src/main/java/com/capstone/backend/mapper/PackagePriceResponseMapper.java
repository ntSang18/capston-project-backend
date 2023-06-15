package com.capstone.backend.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.packagePrice.PackagePriceResponse;
import com.capstone.backend.model.PackagePrice;
import com.capstone.backend.model.PostPayment;

@Service
public class PackagePriceResponseMapper implements Function<PackagePrice, PackagePriceResponse> {

  @Override
  public PackagePriceResponse apply(PackagePrice p) {
    long totalPurchase = 0;
    for (PostPayment payment : p.getPayments()) {
      totalPurchase += payment.getNumberPackage();
    }
    return new PackagePriceResponse(
        p.getId(),
        p.getName(),
        p.getPrice(),
        p.getType().name(),
        p.getNumberOfDays(),
        totalPurchase);
  }

}
