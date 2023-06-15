package com.capstone.backend.service.iservice;

import java.util.List;

import com.capstone.backend.dto.packagePrice.PackagePriceRequest;
import com.capstone.backend.dto.packagePrice.PackagePriceResponse;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.PackagePrice;

public interface IPackagePriceService {

  PackagePrice findById(long id) throws ResourceNotFoundException;

  List<PackagePriceResponse> getPackagePrices();

  PackagePriceResponse getPackagePrice(long id) throws ResourceNotFoundException;

  PackagePriceResponse createPackagePrice(PackagePriceRequest request);

  PackagePriceResponse updatePackagePrice(long id, PackagePriceRequest request) throws ResourceNotFoundException;

  void deletePackagePrice(long id) throws ResourceNotFoundException;

}
