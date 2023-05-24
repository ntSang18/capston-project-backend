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

@Entity(name = "Transaction")
@Table(name = "transactions")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private long money;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @ManyToOne()
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public Transaction(long money, User user) {
    this.money = money;
    this.user = user;
    this.createdAt = LocalDateTime.now();
  }

}
