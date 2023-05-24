package com.capstone.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.backend.model.PostCatalog;

public interface PostCatalogRepository extends JpaRepository<PostCatalog, Long> {

  Optional<PostCatalog> findByName(String name);

}
