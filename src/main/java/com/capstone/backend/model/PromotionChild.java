package com.capstone.backend.model;

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

@Entity(name = "PromotionChild")
@Table(name = "promotion_childs")
@NoArgsConstructor
@Getter
@Setter
public class PromotionChild {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private long startRange;

  @Column(nullable = false)
  private long endRange;

  @Column(nullable = false)
  private int percent;

  @ManyToOne
  @JoinColumn(name = "promotion_id", nullable = false)
  private Promotion promotion;

  public PromotionChild(long start, int percent, Promotion promotion) {
    this.startRange = start;
    this.percent = percent;
    this.promotion = promotion;
  }

}
