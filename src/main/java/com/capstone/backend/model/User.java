package com.capstone.backend.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import com.capstone.backend.constant.Roles;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "User")
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String username;

  @Column(nullable = true)
  private String facebook;

  @Column(nullable = true)
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  private Roles role;

  @Column(nullable = false)
  private long balance;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private boolean isDeleted;

  @Column(nullable = false)
  private boolean enabled;

  @Column(nullable = false)
  private boolean locked;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", nullable = true, referencedColumnName = "id")
  private Address address;

  @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<Token> tokens = new HashSet<Token>();

  public User(String email, String password, String phoneNumber, String username, Roles role) {
    this.email = email;
    this.password = password;
    this.phoneNumber = phoneNumber;
    this.username = username;
    this.role = role;
    this.balance = 0;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.isDeleted = false;
    this.enabled = false;
    this.locked = false;
  }

  public void addToken(Token token) {
    this.tokens.add(token);
  }

  public Boolean removeToken(Token token) {
    return this.tokens.remove(token);
  }

  public Boolean removeTokenIf(Predicate<? super Token> predicate) {
    return this.tokens.removeIf(predicate);
  }
}
