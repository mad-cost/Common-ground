package com.example.common_ground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
          HttpSecurity http
  ) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                            // 로그인
                            .requestMatchers(
                                    "/",
                                    "/login",
                                    "/sign-up",
                                    "/check-email",
                                    "/check-email-token",
                                    "/email-login",
                                    "/check-email-login",
                                    "/login-link",
                                    "/login-by-email",
                                    "/search/study",
                                    "/images/*"
                            )
                            .permitAll()
                            .requestMatchers(
                                    HttpMethod.GET,"/profile/*"
                            ).permitAll()
                            // 나머지는 로그인 해야지만 가능
                            .anyRequest()
                            .authenticated()
            );
    return http.build();
  }
}

