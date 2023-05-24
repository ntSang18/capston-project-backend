package com.capstone.backend.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import com.capstone.backend.constant.PostTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "PackagePrice")
@Table(name = "package_prices")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PackagePrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private int numberOfDays;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PostTypes type;

  @OneToMany(mappedBy = "packagePrice", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<PostPayment> payments = new HashSet<>();

  public PackagePrice(String name, int numberOfDays, PostTypes type) {
    this.name = name;
    this.numberOfDays = numberOfDays;
    this.type = type;
  }

  public void addPostPayment(PostPayment postPayment) {
    this.payments.add(postPayment);
  }

  public Boolean removePostPayment(PostPayment postPayment) {
    return this.payments.remove(postPayment);
  }

  public Boolean removePostPaymentIf(Predicate<? super PostPayment> predicate) {
    return this.payments.removeIf(predicate);
  }
}
