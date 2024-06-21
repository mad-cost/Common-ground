package com.example.common_ground.account.service;

import com.example.common_ground.account.Repository.AccountRepository;
import com.example.common_ground.account.SignUpForm;
import com.example.common_ground.account.entity.Account;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;
  private final JavaMailSender javaMailSender;
  private final PasswordEncoder passwordEncoder;



  /*
  설명 - 16번째 강의 / 16:27 -
  @Transactional을 사용해서 같이 묶여 있으면, saveNewAccount()의 accountRepository.save(account);값이
  return문을 빠져 나와도 generateEmailCheckToken()과 @Transactionl으로 같이 묶여 있으므로
  @Transactionl이 끝나기 전까지 saveNewAccount()의 save()가 유지가 된다.
  즉, generateEmailCheckToken()에서 this.emailCheckToken에 값을 넣어줄 경우 DB에 저장이 된다.
   */
  @Transactional
  public void processNewAccount(SignUpForm signUpForm){
    Account newAccount = saveNewAccount(signUpForm);
    // 가입 성공시 토큰 생성
    newAccount.generateEmailCheckToken();
    sendSignUpConfirmEmail(newAccount);
  }

  private Account saveNewAccount(@Valid SignUpForm signUpForm) {
    // 회원 가입 진행
    Account account = Account.builder()
            .email(signUpForm.getEmail())
            .nickname(signUpForm.getNickname())
            //TODO password는 평문 데이터가 아닌, PasswordEncoding된 Hashing값 저장
            .password(passwordEncoder.encode(signUpForm.getPassword()))
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
    // 메일 본문 url / setText: url로 들어오는 값
    mailMessage.setText(
            "/check-email-token?token=" + newAccount.getEmailCheckToken()
                    + "&email=" + newAccount.getEmail());
    //ConsoleMailSender클래스의 send메서드에서 로그를 만들어 주면 된다.
    javaMailSender.send(mailMessage);
  }





}
