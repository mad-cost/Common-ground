package com.example.common_ground.settings;

import com.example.common_ground.account.entity.Account;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Profile {
  @Size(max = 35)
  private String bio;   // 자기 소개
  private String url;   // 웹사이트 url
  private String occupation;   // 직업
  private String location;   // 사는 곳

  public Profile(Account account) {
    this.bio = account.getBio();
    this.url = account.getUrl();
    this.occupation = account.getOccupation();
    this.location = account.getLocation();
  }
}
