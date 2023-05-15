package com.capstone.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Address")
@Table(name = "addresses")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String province;

  @Column(nullable = false)
  private String district;

  @Column(nullable = false)
  private String commune;

  @Column(nullable = true)
  private String street;

  @Column(nullable = false)
  private String specificAddress;

  @JsonIgnore
  @OneToOne(mappedBy = "address")
  private User user;
}
