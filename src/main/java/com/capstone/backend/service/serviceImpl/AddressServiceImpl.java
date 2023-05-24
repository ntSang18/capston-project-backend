package com.capstone.backend.service.serviceImpl;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.Address;
import com.capstone.backend.repository.AddressRepository;
import com.capstone.backend.service.iservice.IAddressService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {

  private final AddressRepository addressRepository;

  private final MessageSource messageSource;

  @Override
  public Address findById(long id) throws ResourceNotFoundException {
    return addressRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("error.resource-not-found", null, Locale.getDefault())));
  }

  @Override
  public Address save(Address address) {
    return addressRepository.save(address);
  }

}
