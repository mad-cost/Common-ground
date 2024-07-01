package com.example.common_ground.account.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@NoArgsConstructor
@Builder
// Google: city list of korea (wikipedia) / Download(.csv)
public class Zone {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String city; // ex)Andong
  @Column(nullable = false)
  private String localNameOfCity; // ex)안동시
  @Column(nullable = true)
  private String province; // ex)North Gyeongsang (경상 북도)

  @Override
  public String toString() {
    return String.format("%s(%s)/%s", city, localNameOfCity, province);
  }
}





