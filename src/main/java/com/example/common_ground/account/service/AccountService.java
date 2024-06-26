package com.example.common_ground.account.service;

import com.example.common_ground.account.Repository.AccountRepository;
import com.example.common_ground.account.SignUpForm;
import com.example.common_ground.account.entity.Account;
import com.example.common_ground.account.entity.UserAccount;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
  private final AccountRepository accountRepository;
  private final JavaMailSender javaMailSender;
  private final PasswordEncoder passwordEncoder;
//  private final AuthenticationManager authenticationManager;



  /*
  설명 - 16번째 강의 / 16:27 -
  @Transactional을 사용해서 같이 묶여 있으면, saveNewAccount()의 accountRepository.save(account);값이
  return문을 빠져 나와도 generateEmailCheckToken()과 @Transactionl으로 같이 묶여 있으므로
  @Transactionl이 끝나기 전까지 saveNewAccount()의 save()가 유지가 된다.
  즉, generateEmailCheckToken()에서 this.emailCheckToken에 값을 넣어줄 경우 DB에 저장이 된다.
   */
  @Transactional
  public Account processNewAccount(SignUpForm signUpForm){
    Account newAccount = saveNewAccount(signUpForm);
    // 가입 성공시 토큰 생성
    newAccount.generateEmailCheckToken();
    sendSignUpConfirmEmail(newAccount);
    return newAccount;
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


//  private void sendSignUpConfirmEmail(Account newAccount) {
//    // 이메일 보내기
//    SimpleMailMessage mailMessage = new SimpleMailMessage();
//    // 받는 사람 이메일
//    mailMessage.setTo(newAccount.getEmail());
//    // 메일 제목
//    mailMessage.setSubject("스터디올래, 회원 가입 인증");
//    // 메일 본문 url / setText: 터미널 ConsoleMailSender에 들어오는 값
//    mailMessage.setText(
//            "/check-email-token?token=" + newAccount.getEmailCheckToken()
//                    + "&email=" + newAccount.getEmail());
//    //ConsoleMailSender클래스의 send메서드에서 로그를 만들어 주면 된다.
//    javaMailSender.send(mailMessage);
//  }
public void sendSignUpConfirmEmail(Account newAccount) {
  SimpleMailMessage mailMessage = new SimpleMailMessage();
  mailMessage.setTo(newAccount.getEmail());
  mailMessage.setSubject("스터디올래, 회원 가입 인증");
}


  public void login(Account account) {
    // 이렇게 작성하면 실제로 AuthenticationManager가 하는 일과 같다고 볼 수 있다
    // 이렇게 한 이유는 Password를 encoding한 패스워드 밖에 접근하지 못하기 때문
    // 정석적인 방법은 Claim텍스트로 받은 비밀번호를 사용해야 한다.

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(

            // 3개의 값이 와야한다.
//            account.getNickname(), // 1. Principal
            new UserAccount(account),
            account.getPassword(), // 2. Password
            List.of(new SimpleGrantedAuthority("ROLE_USER"))); // 3. 권한
    SecurityContextHolder.getContext().setAuthentication(token);

    /* SecurityContextHolder의 getContext()에서 Authentication설정이 가능하다
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(token); // 권한 설정
    context.getAuthentication(); // 권한 보기
    */


    /* 토큰 인증 정석적인 방법
    UsernamePasswordAuthenticationToken standardToken = new UsernamePasswordAuthenticationToken(
            username, password);
    Authentication authentication = authenticationManager.authenticate(standardToken);
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authentication);
     */
  }


  // Config에서 .formLogin(formLogin -> " ")을 사용하기 위해 필요
  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String emailOrNickname) throws
          UsernameNotFoundException {
    Account account = accountRepository.findByEmail(emailOrNickname);
    if (account == null) {
      account = accountRepository.findByNickname(emailOrNickname);
    }
    // email과 nickname이 모두 null이라면 예외 발생
    if (account == null) {
      throw new UsernameNotFoundException(emailOrNickname);
    }
    // 값이 있을 경우 Principal에 해당하는 객체 반환
    return new UserAccount(account);
  }


  public void completeSignUp(Account account) {
    account.completeSignUp();
    login(account);
  }

}
