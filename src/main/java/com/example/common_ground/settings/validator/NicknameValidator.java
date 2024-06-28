package com.example.common_ground.settings.validator;

import com.example.common_ground.account.Repository.AccountRepository;
import com.example.common_ground.account.entity.Account;
import com.example.common_ground.settings.form.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

  private final AccountRepository accountRepository;

  @Override
  // 어떤 타입의 Form객체를 검증할 것인지 결정
  public boolean supports(Class<?> clazz) {
    // clazz가 NicknameForm이거나 그 하위 클래스라면 true를 반환
    return NicknameForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    NicknameForm nicknameForm = (NicknameForm) target;
    Account byNickname = accountRepository.findByNickname(nicknameForm.getNickname());
    // null이 아니라면 이미 존재하는 nickname이다.
    if (byNickname != null) {
      errors.rejectValue("nickname", "wrong.value", "입력하신 닉네임을 사용할 수 없습니다.");
    }
  }
}