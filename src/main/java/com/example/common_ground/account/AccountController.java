package com.example.common_ground.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {
  
  // 회원 가입
  @GetMapping("/sign-up")
  public String signUp(
          Model model
  ){
    model.addAttribute("signUpForm", new SignUpForm());
    return "account/sign-up";
  }
}
