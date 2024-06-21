package com.example.common_ground.account.controller;
import com.example.common_ground.account.Account;
import com.example.common_ground.account.Repository.AccountRepository;
import com.example.common_ground.account.SignUpForm;
import com.example.common_ground.account.SignUpFormValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {
  private final SignUpFormValidator signUpFormValidator;
  private final AccountRepository accountRepository;
  private final JavaMailSender javaMailSender;
  
  @GetMapping("/")
  public String root(){
    return "home";
  }

  
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
          // @ModelAttribute: SignUpForm(복합 객체)의 여러 필드를 받기 위해서 사용
          @ModelAttribute
          @Valid // 유호성 검사
          SignUpForm signUpForm,
          Errors errors //바인딩 할 때 일어나는 에러를 잡기 위해 사용
  ){
    // 만약 에러가 있다면 다시 회원 가입 페이지로 이동
    if (errors.hasErrors()){
      return "account/sign-up";
    }
    /* signUpFormValidator.validate(signUpForm, errors);
       ↑이 부분을 @Valid SignUpForm에서 @InitBinder를 사용하여 검증 할 수 있다
     */

    // 회원 가입 진행
    Account account = Account.builder()
            .email(signUpForm.getEmail())
            .nickname(signUpForm.getNickname())
            .password(signUpForm.getPassword()) //TODO PasswordEncoding 해야함
            .studyCreatedByWeb(true)
            .studyEnrollmentResultByWeb(true)
            .build();
    Account newAccount = accountRepository.save(account); // 회원 정보 DB에 저장

    // 가입된 정보로 토큰 생성
    newAccount.generateEmailCheckToken();
    // @이메일 보내기 시작
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    // 받는 사람 이메일
    mailMessage.setTo(newAccount.getEmail());
    // 메일 제목
    mailMessage.setSubject("스터디올래, 회원 가입 인증");
    // 메일 본문 url
    mailMessage.setText(
            "/check-email-token?token=" + newAccount.getEmailCheckToken()
            + "email=" + newAccount.getEmail());
    //ConsoleMailSender클래스의 send메서드에서 로그를 만들어 주면 된다.
    javaMailSender.send(mailMessage);
    // 회원 가입 성공
    return "redirect:/";
  }


}
