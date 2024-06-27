package com.example.common_ground.settings;

import com.example.common_ground.account.controller.CurrentUser;
import com.example.common_ground.account.entity.Account;
import com.example.common_ground.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
//@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsController {
  private final AccountService accountService;
  private final ModelMapper modelMapper;

  // 이렇게 사용하는 이유
  // 유지보수: URL 경로를 상수로 정의해 두면, 나중에 경로를 변경해야 할 때 코드 전체를 일일이 찾아 변경할 필요 없이 상수만 수정하면 된다
  static final String SETTINGS_NOTIFICATIONS_VIEW_NAME = "settings/notifications";
  static final String SETTINGS_NOTIFICATIONS_URL = "/settings/notifications";

  static final String SETTINGS_PASSWORD_VIEW_NAME = "settings/password";
  static final String SETTINGS_PASSWORD_URL = "/settings/password";

  static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";
  static final String SETTINGS_PROFILE_URL = "/settings/profile";
  // public: 패키지 밖에서도 사용가능 / protected: 같은 패키지 내부에서 사용가능, SettingsController를 상속 받는 자식 클래스에서도 사용 가능
  // default: 패키지 내에서만 사용가능, 상속 불가능 / private 클래스 내부에서만 사용

  @InitBinder("passwordForm")
  public void initBinder(WebDataBinder webDataBinder){
    webDataBinder.addValidators(new PasswordFormValidator());
  }


  @GetMapping(SETTINGS_PROFILE_URL)
  // @GetMapping("/settings/profile")
  public String updateProfileForm(
          // @CurrentUser: 현재 인증된 사용자의 정보를 가져오는데 사용
          @CurrentUser
          Account account,
          Model model
  ){
    model.addAttribute(account);
    // ModelMapper 사용 전: model.addAttribute(new Profile(account));
    model.addAttribute(modelMapper.map(account, Profile.class));
    return SETTINGS_PROFILE_VIEW_NAME;
  }

  @PostMapping(SETTINGS_PROFILE_URL)
  public String updateProfile(
          // @CurrentUser: 현재 인증된 사용자의 정보를 가져오는데 사용
          @CurrentUser
          Account account,
          // @@@ - 여기부터
          @Valid
          @ModelAttribute
          // @ModelAttribute: SignUpForm(복합 객체)의 여러 필드를 받기 위해서 사용
          Profile profile,
          Errors errors,
          // @@@ - 여기까지는 세트로 가져간다고 생각하자
          Model model,
          // 리다렉트해서 수정해도 티가 안나서 메시지 뛰워주기
          RedirectAttributes
          attributesData
  ){
    if (errors.hasErrors()){
      model.addAttribute(account);
      return SETTINGS_PROFILE_VIEW_NAME;
    }

    accountService.updateProfile(account, profile);
    // 리다이렉트시 간단한 데이터를 전달하기 위해 사용 (일회용 데이터)
    attributesData.addFlashAttribute("message", "프로필을 수정했습니다");
    //return "redirect:" + "/settings/profile";
    return "redirect:" + SETTINGS_PROFILE_URL;
  }

  @GetMapping(SETTINGS_PASSWORD_URL)
  public String updatePasswordForm(
          @CurrentUser
          Account account,
          Model model
  ){
      model.addAttribute(account);
      model.addAttribute(new PasswordForm());
      return SETTINGS_PASSWORD_VIEW_NAME;
  }

  @PostMapping(SETTINGS_PASSWORD_URL)
  public String updatePassword(
          @CurrentUser
          Account account,
          @Valid
          PasswordForm passwordForm,
          Errors errors,
          Model model,
          RedirectAttributes attributes
  ){
    if (errors.hasErrors()){
      model.addAttribute(account);
      return SETTINGS_PASSWORD_VIEW_NAME;
    }
    accountService.updatePassword(account, passwordForm.getNewPassword());
    // 업데이트가 잘 됐다면 메세지 일회용 메시지 띄우기
    attributes.addFlashAttribute("message", "패스워드를 변경했습니다");
    return "redirect:" + SETTINGS_PASSWORD_URL;
  }


  @GetMapping(SETTINGS_NOTIFICATIONS_URL)
  public String updateNotificationsForm(
          @CurrentUser
          Account account,
          Model model
  ) {
    model.addAttribute(account);
    // 폼을 채울 객체
    // ModelMapper 사용 전: model.addAttribute(new Notifications(account));
    model.addAttribute(modelMapper.map(account, Notifications.class));
    return SETTINGS_NOTIFICATIONS_VIEW_NAME;
  }

  @PostMapping(SETTINGS_NOTIFICATIONS_URL)
  public String updateNotifications(
          @CurrentUser
          Account account,
          @Valid
          Notifications notifications,
          Errors errors,
          Model model,
          RedirectAttributes attributes) {
    if (errors.hasErrors()) {
      model.addAttribute(account);
      return SETTINGS_NOTIFICATIONS_VIEW_NAME;
    }

    accountService.updateNotifications(account, notifications);
    attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
    return "redirect:" + SETTINGS_NOTIFICATIONS_URL;
  }

}
