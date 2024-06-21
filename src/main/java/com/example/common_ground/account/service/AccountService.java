package com.example.common_ground.account.service;

import com.example.common_ground.account.Repository.AccountRepository;
import com.example.common_ground.account.SignUpForm;
import com.example.common_ground.account.entity.Account;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;
  private final JavaMailSender javaMailSender;


  public void processNewAccount(SignUpForm signUpForm){
    Account newAccount = saveNewAccount(signUpForm);
    newAccount.generateEmailCheckToken();
    sendSignUpConfirmEmail(newAccount);
  }

  private Account saveNewAccount(@Valid SignUpForm signUpForm) {
    // 회원 가입 진행
    Account account = Account.builder()
            .email(signUpForm.getEmail())
            .nickname(signUpForm.getNickname())
            .password(signUpForm.getPassword()) //TODO PasswordEncoding 해야함
            .studyCreatedByWeb(true)
            .studyEnrollmentResultByWeb(true)
            .build();
    return accountRepository.save(account); // 회원 정보 DB에 저장
  }


  private void sendSignUpConfirmEmail(Account newAccount) {
    // 이메일 보내기
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
  }





}
