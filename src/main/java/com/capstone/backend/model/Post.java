package com.capstone.backend.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import com.capstone.backend.constant.PostStatuses;
import com.capstone.backend.constant.PostTargets;
import com.capstone.backend.constant.PostTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Post")
@Table(name = "posts")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, length = 2048)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private long price;

  @Column(nullable = false)
  private long deposit;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PostTargets target;

  @Column(nullable = false)
  private long acreage;

  @Column(nullable = true)
  @Enumerated(EnumType.STRING)
  private PostTypes type;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PostStatuses status;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = true)
  private LocalDateTime confirmedAt;

  @Column(nullable = true)
  private LocalDateTime declinedAt;

  @Column(nullable = true)
  private LocalDateTime paidAt;

  @Column(nullable = true)
  private LocalDateTime expiredAt;

  @Column(nullable = false)
  private boolean isDeleted;

  @Column(nullable = true, columnDefinition = "TEXT")
  private String refusedReason;

  @ManyToOne()
  @JoinColumn(nullable = false, name = "catalog_id")
  private PostCatalog catalog;

  @ManyToOne()
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;

  @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<PostMedia> medias = new HashSet<>();

  @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<PostPayment> payments = new HashSet<>();

  public Post(
      String title,
      String description,
      long price,
      long deposit,
      PostTargets target,
      long acreage,
      PostCatalog catalog,
      User user,
      Address address) {
    this.title = title;
    this.description = description;
    this.price = price;
    this.deposit = deposit;
    this.target = target;
    this.acreage = acreage;
    this.status = PostStatuses.UNCONFIRMED;
    this.catalog = catalog;
    this.isDeleted = false;
    this.user = user;
    this.address = address;
    this.createdAt = LocalDateTime.now();
  }

  public void addMedia(PostMedia media) {
    this.medias.add(media);
  }

  public Boolean removeMedia(PostMedia media) {
    return this.medias.remove(media);
  }

  public Boolean removeMediaIf(Predicate<? super PostMedia> predicate) {
    return this.medias.removeIf(predicate);
  }

  public void addPayment(PostPayment payment) {
    this.payments.add(payment);
  }

  public Boolean removePayment(PostPayment payment) {
    return this.payments.remove(payment);
  }

  public Boolean removePaymentIf(Predicate<? super PostPayment> predicate) {
    return this.payments.removeIf(predicate);
  }
}
