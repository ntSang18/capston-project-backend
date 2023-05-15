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

@Entity(name = "Token")
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime expiredAt;

  private LocalDateTime confirmAt;

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  public Token(String token, LocalDateTime createdAt, LocalDateTime expiredAt, User user) {
    this.token = token;
    this.createdAt = createdAt;
    this.expiredAt = expiredAt;
    this.user = user;
  }
}
