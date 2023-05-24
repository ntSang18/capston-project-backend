package com.capstone.backend.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.capstone.backend.dto.address.AddressDto;
import com.capstone.backend.model.Address;

@Service
public class AddressDtoMapper implements Function<Address, AddressDto> {

  @Override
  public AddressDto apply(Address address) {
    return new AddressDto(
        address.getProvince(),
        address.getDistrict(),
        address.getWard(),
        address.getSpecificAddress());
  }

}
