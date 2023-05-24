package com.capstone.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "PostPayment")
@Table(name = "post_payments")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostPayment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private LocalDateTime paidAt;

  @ManyToOne()
  @JoinColumn(nullable = false, name = "post_id")
  private Post post;

  @ManyToOne()
  @JoinColumn(nullable = false, name = "package_price_id")
  private PackagePrice packagePrice;

  public PostPayment(Post post, PackagePrice packagePrice) {
    this.post = post;
    this.packagePrice = packagePrice;
    this.paidAt = LocalDateTime.now();
  }
}
