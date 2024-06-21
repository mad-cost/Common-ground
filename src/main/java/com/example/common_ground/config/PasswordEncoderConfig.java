package com.example.common_ground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// 회원 가입 성공시(repository.save()) DB에 Password는 평문 값이 아닌 hashing된 값을 넣어줘야 한다
// 내가 입력한 password + salt = 해싱된 값
public class PasswordEncoderConfig {

  @Bean
  public PasswordEncoder passwordEncoder(){
//    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    return new BCryptPasswordEncoder();
  }
}
