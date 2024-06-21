package com.example.common_ground.account;

import lombok.Data;

@Data
// 회원 가입시 받아 올 데이터
public class SignUpForm {
  private String nickname;
  private String email;
  private String password;
}
