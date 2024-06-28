package com.example.common_ground.settings.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NicknameForm {
  @NotBlank
  @Size(min = 3, max = 20)
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
  private String nickname;

}
