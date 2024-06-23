package com.example.common_ground.account;

import com.example.common_ground.account.Repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
  // Controller를 Test할 때 사용
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private AccountRepository accountRepository;
  // 이메일 보내는지 확인
  @MockBean
  JavaMailSender javaMailSender;

  @DisplayName("인증 메일 확인 - 입력값 오류")
  @Test
  void checkEmailToken_with_wrong_input() throws Exception {
    mockMvc.perform(get("/check-email-token")
                    .param("token", "sdfjslwfwef")
                    .param("email", "email@email.com"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("error"))
            .andExpect(view().name("account/checked-email"))
            .andExpect(unauthenticated());
  }

  // 테스트 메서드의 이름을 지정
  @DisplayName("회원 가입 화면 보이는지 테스트")
  @Test
  void signUpForm() throws Exception {
    mockMvc.perform(get("/sign-up"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("account/sign-up"))
            .andExpect(model().attributeExists("signUpForm"))
            .andExpect(unauthenticated());
  }


  @DisplayName("회원 가입 처리 - 입력값 오류")
  @Test
  void signUpSubmit_with_wrong_input() throws Exception {
    mockMvc.perform(post("/sign-up")
                    .param("nickname", "testJun")
                    .param("email", "email..")
                    .param("password", "12345")
                    .with(csrf()))
            .andExpect(status().isOk())
            // 회원 가입 실패시 이동하는 뷰
            .andExpect(view().name("account/sign-up"))
            .andExpect(unauthenticated());
  }

  @DisplayName("회원 가입 처리 - 입력값 정상")
  @Test
  void signUpSubmit_with_correct_input() throws Exception {
    mockMvc.perform(post("/sign-up")
                    .param("nickname", "testJun")
                    .param("email", "testJun@email.com")
                    .param("password", "12345678")
                    .with(csrf()))
            .andExpect(status().is3xxRedirection())
            // 회원 가입 성공시 이동하는 뷰
            .andExpect(view().name("redirect:/"))
            .andExpect(authenticated().withUsername("testJun"));
  }




}
