package com.example.common_ground.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.spi.NameTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  /* 영한님: 아... 저는 modelMapper를 좋아하지 않는 편입니다.
     modelMapper가 룰이 복잡하기도 하고, 또 자동화 되는 것이 참 좋기는 하지만, 컴파일 시점에 오류를 잡을 수 없어서요^^!
     물론 장점도 많으니 팀에서도 누가 쓴다고 할때 말리지는 않습니다^^! */
  // 혼자 만들 때는 static factory method pattern을 고려하는게 더 좋은 방법 일 수 있을꺼 같다
  @Bean
  // ModelMapper: 객체 간의 변환을 쉽게 해주는 라이브러리
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    // ModelMapper 설정 가져오기
    modelMapper.getConfiguration()
            // ModelMapper가 객체의 필드 이름을 토큰화할 때, 필드 이름을 UNDERSCORE(_) 기준으로 나눈다
            // ex) 객체의 필드 이름이 first_name이라면, 이 필드를 first와 name이라는 두 개의 토큰으로 분리
            .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
            // 원본 객체 필드 이름을 토큰화 할 때도 이하동문
            .setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
    return modelMapper;
  }



}
