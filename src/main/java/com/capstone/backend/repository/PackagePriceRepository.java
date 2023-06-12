package com.capstone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.backend.model.PackagePrice;

public interface PackagePriceRepository extends JpaRepository<PackagePrice, Long> {

}
