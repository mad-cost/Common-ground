package com.example.common_ground.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
  // Controller를 Test할 때 사용
  @Autowired
  private MockMvc mockMvc;

  // 테스트 메서드의 이름을 지정
  @DisplayName("회원 가입 화면 보이는지 테스트")
  @Test
  void  signUpForm() throws Exception {
    mockMvc.perform(get("/sign-up"))
            .andExpect(status().isOk())
            .andExpect(view().name("account/sign-up"));
  }
}
