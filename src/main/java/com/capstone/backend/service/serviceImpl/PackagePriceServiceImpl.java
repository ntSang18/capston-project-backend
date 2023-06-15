package com.capstone.backend.service.serviceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.capstone.backend.constant.PostTypes;
import com.capstone.backend.dto.packagePrice.PackagePriceRequest;
import com.capstone.backend.dto.packagePrice.PackagePriceResponse;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.mapper.PackagePriceResponseMapper;
import com.capstone.backend.model.PackagePrice;
import com.capstone.backend.repository.PackagePriceRepository;
import com.capstone.backend.service.iservice.IPackagePriceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackagePriceServiceImpl implements IPackagePriceService {

  private final PackagePriceRepository packagePriceRepository;

  private final PackagePriceResponseMapper packagePriceResponseMapper;

  private final MessageSource messageSource;

  @Override
  public PackagePrice findById(long id) throws ResourceNotFoundException {
    PackagePrice packagePrice = packagePriceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));

    if (packagePrice.isDeleted()) {
      throw new ResourceNotFoundException(
          messageSource.getMessage("error.resource-not-found", null, Locale.getDefault()));
    }

    return packagePrice;
  }

  @Override
  public List<PackagePriceResponse> getPackagePrices() {
    return packagePriceRepository.findAll()
        .stream()
        .filter(p -> !p.isDeleted())
        .sorted(Comparator.comparingLong(PackagePrice::getId))
        .map(packagePriceResponseMapper)
        .collect(Collectors.toList());
  }

  @Override
  public PackagePriceResponse getPackagePrice(long id) throws ResourceNotFoundException {
    return packagePriceResponseMapper.apply(findById(id));
  }

  @Override
  public PackagePriceResponse createPackagePrice(PackagePriceRequest request) {
    PackagePrice packagePrice = packagePriceRepository.save(
        new PackagePrice(
            request.name(),
            request.price(),
            request.numberOfDays(),
            PostTypes.valueOf(request.type())));

    return packagePriceResponseMapper.apply(packagePrice);
  }

  @Override
  public PackagePriceResponse updatePackagePrice(long id, PackagePriceRequest request)
      throws ResourceNotFoundException {
    PackagePrice packagePrice = findById(id);

    packagePrice.setName(request.name());
    packagePrice.setPrice(request.price());
    packagePrice.setType(PostTypes.valueOf(request.type()));
    packagePrice.setNumberOfDays(request.numberOfDays());

    return packagePriceResponseMapper.apply(packagePriceRepository.save(packagePrice));

  }

  @Override
  public void deletePackagePrice(long id)
      throws ResourceNotFoundException {
    PackagePrice packagePrice = findById(id);

    packagePrice.setDeleted(true);
    packagePriceRepository.save(packagePrice);
  }

}
