package com.example.common_ground.account.controller;
import com.example.common_ground.account.entity.Account;
import com.example.common_ground.account.Repository.AccountRepository;
import com.example.common_ground.account.SignUpForm;
import com.example.common_ground.account.SignUpFormValidator;
import com.example.common_ground.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {
  private final SignUpFormValidator signUpFormValidator;
  private final AccountService accountService;
  private final AccountRepository accountRepository;

  
//  @GetMapping("/")
//  public String root(){
//    return "index";
//  }

  // 회원 가입
  @GetMapping("/sign-up")
  public String signUp(
          Model model
  ){
    // SignUpForm()객체를 사용
    model.addAttribute("signUpForm", new SignUpForm());
    return "account/sign-up";
  }

  @InitBinder("signUpForm")
  /* @InitBinder("signUpForm"):
  [오해 금지] @InitBinder("signUpForm")의 이름은
  @Valid SignUpForm signUpForm 에서 SignUpForm의 변수 명이 아닌, @Valid SignUpForm클래스의 카멜클래스를 따라간다.
   */
  public void initBinder(WebDataBinder webDataBinder){
    webDataBinder.addValidators(signUpFormValidator);
  }

  @PostMapping("/sign-up")
  public String signUpSubmit(
          @Valid // 유호성 검사
          @ModelAttribute
          // @ModelAttribute: SignUpForm(복합 객체)의 여러 필드를 받기 위해서 사용
          SignUpForm signUpForm, //  @InitBinder("signUpForm")사용시 실행된다
          Errors errors //바인딩 할 때 일어나는 에러를 잡기 위해 사용
  ){
    // 만약 에러가 있다면 다시 회원 가입 페이지로 이동
    if (errors.hasErrors()){
      return "account/sign-up";
    }
    /* signUpFormValidator.validate(signUpForm, errors);
       ↑이 부분을 @Valid SignUpForm에서 @InitBinder를 사용하여 검증 할 수 있다
     */
    // 회원 가입에 문제가 없다면 진행
    Account account = accountService.processNewAccount(signUpForm);
    accountService.login(account);
    // AccountService.login()의 '3.권한' 값
    log.info("@@@  권한"+ SecurityContextHolder.getContext().getAuthentication().toString());

    // 회원 가입 성공
    return "/index";
  }

  @GetMapping("/check-email-token")
  public String checkEmailToken(
          String token,
          String email,
          Model model
  ){
    // 이메일에 해당하는 유저 검증
    Account account = accountRepository.findByEmail(email);
    String view = "account/checked-email";

    if (account == null){
      model.addAttribute("error", "wrong.email");
      return view;
    }
    // 이메일이 null이 아니지만 / DB의 토큰과 받은 값의 토큰이 같지 않다면 오류
    if(!account.isValidToken(token)){
      model.addAttribute("error", "wrong.token");
      return view;
    }

    accountService.completeSignUp(account);
    /* account가 검증 됐다면
    account.completeSignUp();
    accountService.login(account);
     */

    // 몇 번째 가입 계정인가
    model.addAttribute("numberOfUser", accountRepository.count());
    model.addAttribute("nickname", account.getNickname());
    return view;
  }

  @GetMapping("/check-email")
  public String checkEmail(
          // @CurrentUser: 인증된 사용자의 정보를 가져오는 데 사용
          @CurrentUser
          Account account,
          Model model) {
    model.addAttribute("email", account.getEmail());
    return "account/check-email";
  }

  @GetMapping("/resend-confirm-email")
  public String resendConfirmEmail(@CurrentUser Account account, Model model) {
    if (!account.canSendConfirmEmail()) {
      model.addAttribute("error", "인증 이메일은 1시간에 한번만 전송할 수 있습니다.");
      model.addAttribute("email", account.getEmail());
      return "account/check-email";
    }

    accountService.sendSignUpConfirmEmail(account);
    return "redirect:/";
  }

// 프로필
  @GetMapping("/profile/{nickname}")
  public String viewProfile(
          @PathVariable
          String nickname,
          Model model,
//        현재 프로필을 보고 있는 유저가 프로필의 주인인지 아닌지 알기 위해
          @CurrentUser
          Account account
  ){
    Account byNickname = accountRepository.findByNickname(nickname);
    if (byNickname == null){
      throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다");
    }

    model.addAttribute(byNickname);
    model.addAttribute("isOwner", byNickname.equals(account));

    return "account/profile";
  }





}
