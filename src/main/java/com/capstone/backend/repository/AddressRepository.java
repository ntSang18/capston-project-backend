package com.capstone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.backend.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
