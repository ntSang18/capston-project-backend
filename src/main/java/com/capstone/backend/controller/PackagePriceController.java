package com.capstone.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.backend.dto.packagePrice.PackagePriceRequest;
import com.capstone.backend.dto.packagePrice.PackagePriceResponse;
import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.service.iservice.IPackagePriceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/package-price")
public class PackagePriceController {

  private final IPackagePriceService packagePriceService;

  @GetMapping(value = "/public")
  public ResponseEntity<?> getPackagePrices() {
    List<PackagePriceResponse> responses = packagePriceService.getPackagePrices();
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getPackagePrice(@PathVariable long id)
      throws ResourceNotFoundException {
    PackagePriceResponse response = packagePriceService.getPackagePrice(id);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> createPackagePrice(@RequestBody PackagePriceRequest request) {
    PackagePriceResponse response = packagePriceService.createPackagePrice(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping(value = "/{id}")
  public ResponseEntity<?> updatePackagePrice(
      @PathVariable long id,
      @RequestBody PackagePriceRequest request)
      throws ResourceNotFoundException {
    PackagePriceResponse response = packagePriceService.updatePackagePrice(id, request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deletePackagePrice(@PathVariable long id)
      throws ResourceNotFoundException {
    packagePriceService.deletePackagePrice(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
