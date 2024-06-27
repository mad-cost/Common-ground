package com.example.common_ground.account.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
// equals: 두 객체의 내용이 같은지 동등성을 비교하고
// hashCode: 두 객체가 같은 객체인지 동일성을 비교한다.
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

  // 이메일 검증된 계정인지
  private boolean emailVerified;
  // 이메일 검증시 사용할 토큰
  private String emailCheckToken;
  private LocalDateTime emailCheckTokenGeneratedAt;
  private LocalDateTime joinedAt;   // 가입 날짜

  private String bio;   // 자기 소개
  private String url;   // 웹사이트 url
  private String occupation;   // 직업
  private String location;   // 거주지

  @Lob // 이미지 크기가 String = varchar(255)를 넘을꺼 같을 경우 사용.
  @Basic(fetch = FetchType.EAGER) // 이 필드에 접근할 때마다 프로필 이미지 데이터를 즉시 가져오겠다는 의미
  private String profileImage;

  // 스터디 개설, 이메일로 알림 받기
  private boolean studyCreatedByEmail;
  // 스터디 개설, 웹으로 알림 받기
  private boolean studyCreatedByWeb = true;
  // 스터디 등록(가입), 이메일로 알림 받기
  private boolean studyEnrollmentResultByEmail;
  // 스터디 등록, 웹으로 알림 받기
  private boolean studyEnrollmentResultByWeb = true;
  // 스터디 수정 정보, 이메일로 알림 받기
  private boolean studyUpdateByEmail;
  // 스터디 수정 정보, 웹으로 알림 받기
  private boolean studyUpdateByWeb = true;

  // 가입된 정보로 토큰 생성
  public void generateEmailCheckToken() {
    // UUID를 사용해서 emailCheckToken(이메일 검증시 사용할 토큰)에 랜덤으로 문자열 값 생성
    this.emailCheckToken = UUID.randomUUID().toString();
    this.emailCheckTokenGeneratedAt = LocalDateTime.now();
  }

  public void completeSignUp() {
    // 검증된 계정
    this.emailVerified = true;
    // account.setEmailVerified(true); -> @Controller에서 메서드로 빼기 전 상태

    // 가입 날짜
    this.joinedAt = LocalDateTime.now();
    // account.setJoinedAt(LocalDateTime.now());
  }

  public boolean isValidToken(String token) {
    return this.emailCheckToken.equals(token);
  }

  public boolean canSendConfirmEmail() {
    return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
  }
}
