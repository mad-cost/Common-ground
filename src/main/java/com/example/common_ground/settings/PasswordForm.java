package com.example.common_ground.settings;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordForm {
  @Size(min = 8, max = 50)
  private String newPassword;
  @Size(min = 8, max = 50)
  private String newPasswordConfirm;
}
