package com.example.common_ground.main;


import com.example.common_ground.account.controller.CurrentUser;
import com.example.common_ground.account.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  @GetMapping("/")
  public String home(
          @CurrentUser
          Account account,
          Model model
          ){
    // account가 null이 아니라면 인증이 된 사용자 이므로
    // model에 account를 넣어준다
    if (account != null){
      model.addAttribute(account);
    }

    return "index";
  }

  @GetMapping("/login")
  public String login(){
    return "login";
  }


}
