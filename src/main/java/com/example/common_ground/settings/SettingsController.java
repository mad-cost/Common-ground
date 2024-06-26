package com.example.common_ground.settings;

import com.example.common_ground.account.controller.CurrentUser;
import com.example.common_ground.account.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {

  @GetMapping("/settings/profile")
  public String profileUpdateForm(
          @CurrentUser
          Account account,
          Model model
  ){
    model.addAttribute(account);
    model.addAttribute(new Profile(account));
    return "settings/profile";
  }

}
