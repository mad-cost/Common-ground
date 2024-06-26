package com.example.common_ground.main;

import com.example.common_ground.account.Repository.AccountRepository;
import com.example.common_ground.account.SignUpForm;
import com.example.common_ground.account.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  AccountService accountService;
  @Autowired
  AccountRepository accountRepository;


  // 중복코드, 모든 테스트가 실행될 때 먼저 실행된다
  // 단점: 닉네임과 이메일이 중복돼서 DB에 들어감
  @BeforeEach
  void beforeEach() {
    SignUpForm signUpForm = new SignUpForm();
    signUpForm.setNickname("keesun");
    signUpForm.setEmail("keesun@email.com");
    signUpForm.setPassword("12345678");
    accountService.processNewAccount(signUpForm);
  }

  // 단점 보완: 닉네임과 이메일 중복 제거
  @AfterEach
  void afterEach() {
    accountRepository.deleteAll();
  }

  @DisplayName("이메일로 로그인 성공")
  @Test
  void login_with_email() throws Exception {
    mockMvc.perform(post("/login")
                    .param("username", "keesun@email.com")
                    .param("password", "12345678")
                    .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andExpect(authenticated().withUsername("keesun"));
  }

  @DisplayName("이메일로 로그인 성공")
  @Test
  void login_with_nickname() throws Exception {
    mockMvc.perform(post("/login")
                    .param("username", "keesun")
                    .param("password", "12345678")
                    .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andExpect(authenticated().withUsername("keesun"));
  }

  @DisplayName("로그인 실패")
  @Test
  void login_fail() throws Exception {
    mockMvc.perform(post("/login")
                    .param("username", "111111")
                    .param("password", "000000000")
                    .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"))
            .andExpect(unauthenticated());
  }

  @WithMockUser
  @DisplayName("로그아웃")
  @Test
  void logout() throws Exception {
    mockMvc.perform(post("/logout")
                    .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andExpect(unauthenticated());
  }

}

