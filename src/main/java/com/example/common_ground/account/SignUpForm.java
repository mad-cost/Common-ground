package com.example.common_ground.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
// 회원 가입시 받아 올 데이터
public class SignUpForm {
  @NotBlank // 값이 비어있으면 안된다
  @Size(min = 3, max = 20) // 글자 수: 3 ~ 20
  // 닉네임 패턴으로 쓸 수 있는 것들 / 패턴 모습: ^(시작) [내용] {3개부터 20개} $(끝)
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$") // 한글, 영어, 숫자, _언더바, -하이픈
  private String nickname;

  @Email // 이메일 형식의 문자열이어야 한다
  @NotBlank
  private String email;

  @NotBlank
  @Size(min = 8, max = 50)
  private String password;
}
