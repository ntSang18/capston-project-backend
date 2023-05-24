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
import lombok.ToString;

@Entity(name = "PostCatalog")
@Table(name = "post_catalogs")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostCatalog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private boolean isDeleted;

  @OneToMany(mappedBy = "catalog", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<Post> posts = new HashSet<Post>();

  public PostCatalog(String name) {
    this.name = name;
    this.isDeleted = false;
    this.createdAt = LocalDateTime.now();
  }

  public void addPost(Post post) {
    this.posts.add(post);
  }

  public Boolean removePost(Post post) {
    return this.posts.remove(post);
  }

  public Boolean removeMediaIf(Predicate<? super Post> predicate) {
    return this.posts.removeIf(predicate);
  }
}
