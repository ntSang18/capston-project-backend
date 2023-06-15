package com.capstone.backend.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Promotion")
@Table(name = "promotions")
@NoArgsConstructor
@Getter
@Setter
public class Promotion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDateTime startAt;

  @Column(nullable = false)
  private LocalDateTime endAt;

  @Column(nullable = false)
  private boolean isDeleted;

  @OneToMany(mappedBy = "promotion", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<PromotionChild> childs = new HashSet<PromotionChild>();

  public Promotion(String name, LocalDateTime start, LocalDateTime end) {
    this.name = name;
    this.startAt = start;
    this.endAt = end;
    this.isDeleted = false;
  }

  public void addChild(PromotionChild child) {
    this.childs.add(child);
  }

  public Boolean removeChild(PromotionChild child) {
    return this.childs.remove(child);
  }

  public void removeAllChild() {
    this.childs.clear();
  }

  public Boolean removeChildIf(Predicate<? super PromotionChild> predicate) {
    return this.childs.removeIf(predicate);
  }

}
