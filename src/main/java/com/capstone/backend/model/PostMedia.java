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
import lombok.ToString;

@Entity(name = "PostMedia")
@Table(name = "post_medias")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostMedia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String url;

  @ManyToOne()
  @JoinColumn(nullable = false, name = "post_id")
  private Post post;

  public PostMedia(String url, Post post) {
    this.url = url;
    this.post = post;
  }

  public PostMedia(Post post) {
    this.post = post;
    this.url = "";
  }
}
