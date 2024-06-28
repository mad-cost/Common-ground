package com.example.common_ground.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@EqualsAndHashCode( of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
  @Id
  @GeneratedValue
  private Long id;

  // 중복X, null 허용X
  @Column(unique = true, nullable = false)
  private String title;

}
