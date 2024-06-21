package com.example.common_ground.account;

import com.example.common_ground.account.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component // 빈으로 등록: 스프링은 빈과 빈들만 의존성을 주입 받을 수 있다
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {
  private final AccountRepository accountRepository;
    // AccountRepository의 생성자
//  public SignUpFormValidator(AccountRepository accountRepository) {
//    this.accountRepository = accountRepository;}

  @Override
  // 주어진 클래스가 SignUpForm 클래스와 호환 가능한지 여부를 판단하고, 호환 가능하면 true를 반환
  public boolean supports(Class<?> clazz) {
    return clazz.isAssignableFrom(SignUpForm.class);
  }

  @Override
  public void validate(Object target, Errors errors) {
    // TODO email, nickname이 중복 되는지 검사하기
    // if()를 통해서 SignUpForm에 대한 errors 처리
    //@PostMapping("/sign-up")에서 사용
    SignUpForm signUpForm = (SignUpForm)target;
    if (accountRepository.existsByEmail(signUpForm.getEmail())) {
      //데이터베이스에 존재하는지를 검사하고, 만약 존재한다면 해당 필드에 대한 오류를 추가
      errors.rejectValue(
              "email",
              "invalid.email Error",
              new Object[]{signUpForm.getEmail()},
              "이메일 이미 사용중인 이메일 입니다");

      /*
      errors.rejectValue()는 주로 스프링의 폼 유효성 검사에서 사용. 데이터베이스와의 상호 작용 결과에 따라 오류를 폼 데이터에 연결하여 사용자에게 보여준다.
      ↓아래 코드는 예외 상황을 처리할 때 주로 사용됩니다. 예외를 던지면 해당 메서드의 실행 흐름이 중단되고, 예외 처리 기능을 사용하여 오류를 처리하게 됩니다.
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
      
      예외를 직접 던지는 방식은 예외적인 상황에서 사용되며, 특정 예외 처리 로직이 필요할 때 유용.
      따라서 일반적으로는 폼 데이터의 유효성 검사와 관련된 오류 처리에는 errors.rejectValue()를 사용하는 것이 권장된다.
       */
    }

    if (accountRepository.existsByNickname(signUpForm.getNickname())){
      errors.rejectValue(
              "nickname",
              "invalid.nickname Error",
              new Object[]{signUpForm.getEmail()},
              "닉네임이 이미 사용중인 닉네임 입니다");
    }
  }

}
