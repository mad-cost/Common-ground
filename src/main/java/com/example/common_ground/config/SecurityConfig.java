package com.example.common_ground.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
          throws Exception {
    http
            .authorizeHttpRequests((authz) -> authz
                            .requestMatchers("/",
                                    "/login",
                                    "/sign-up",
                                    "/check-email-token",
                                    "/email-login",
                                    "/check-email-login",
                                    "/login-link").permitAll() // 누구나 접근 가능

                            .requestMatchers(HttpMethod.GET,
                                    "/profile/*")
                            .hasRole("USER") // USER 역할만 접근 가능
                            .anyRequest()
                            .authenticated() // 나머지 요청은 인증 필요

            )
            .formLogin(formLogin ->
                    formLogin.loginPage("/login").permitAll())
            .logout(formLogout ->
                    formLogout.logoutSuccessUrl("/"));
//.formLogin(withDefaults()) // 기본 로그인 폼 설정
//.logout(withDefaults()); // 기본 로그아웃 설정
    return http.build();
  }
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
            .requestMatchers("/node_modules/**")
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }
}
