package com.capstone.backend.service.iservice;

import com.capstone.backend.exception.ResourceNotFoundException;
import com.capstone.backend.model.Address;

public interface IAddressService {

  Address findById(long id) throws ResourceNotFoundException;

  Address save(Address address);
}
