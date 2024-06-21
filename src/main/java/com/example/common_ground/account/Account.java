package com.example.common_ground.account;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
// id 필드만을 기준으로 equals()와 hashCode()를 생성하도록 지정
@EqualsAndHashCode( of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 로그인 방식 / email, nickname
  @Column(unique = true)
  private String email;
  @Column(unique = true)
  private String nickname;
  private String password;

  // 이메일 인증이 된 계정인지
  private boolean emailVerified;
  // 이메일 검증시 사용할 토큰
  private String emailCheckToken;

  private LocalDateTime joinedAt;   // 가입 날짜
  private String bio;   // 자기 소개
  private String url;   // 웹사이트 url
  private String occupation;   // 직업
  private String location;   // 거주지
  @Lob // 이미지 크기가 String = varchar(255)를 넘을 수 있다.
  @Basic(fetch = FetchType.EAGER) // 이 필드에 접근할 때마다 프로필 이미지 데이터를 즉시 가져오겠다는 의미
  private String profileImage;

  // 스터디 개설, 이메일로 알림 받기
  private boolean studyCreatedByEmail;
  // 스터디 개설, 웹으로 알림 받기
  private boolean studyCreatedByWeb;
  // 스터디 등록(가입), 이메일로 알림 받기
  private boolean studyEnrollmentResultByEmail;
  // 스터디 등록, 웹으로 알림 받기
  private boolean studyEnrollmentResultByWeb;
  // 스터디 수정 정보, 이메일로 알림 받기
  private boolean studyUpdateByEmail;
  // 스터디 수정 정보, 웹으로 알림 받기
  private boolean studyUpdateByWeb;

}
