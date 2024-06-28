package com.example.common_ground.settings.validator;

import com.example.common_ground.settings.form.PasswordForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PasswordFormValidator implements Validator {

  @Override
  // 어떤 타입의 Form객체를 검증할 것인지 결정
  // 주어진 클래스 타입이 PasswordForm 클래스나 그 하위 클래스인지 여부를 확인
  // supports(): 특정 클래스 타입이 PasswordForm 클래스 또는 그 하위 클래스인지 확인
  public boolean supports(Class<?> clazz) {
    // clazz가 PasswordForm이거나 그 하위 클래스라면 true를 반환
    return PasswordForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PasswordForm passwordForm = (PasswordForm) target;
    // 비밀번호, 비밀번호 확인이 다른가?
    if (!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfirm())){
      errors.rejectValue(
              "newPassword",
              "wrong.value",
              "입력한 새 패스워드가 일치하지 않습니다");
    }
  }
}
