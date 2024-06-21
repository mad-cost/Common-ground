package com.example.common_ground;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.InputStream;
// @Profile("local"): "local"이라는 프로파일(yaml)이 활성화된 경우에만 해당 빈이 등록되고 사용될 수 있음을 의미
@Profile("local")
@Component
@Slf4j
public class ConsoleMailSender implements JavaMailSender {
  // 지금은 객체를 사용하고 나중에는 주입을 받아서 사용해보자

  @Override
  public MimeMessage createMimeMessage() {
    return null;
  }

  @Override
  public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
    return null;
  }

  @Override
  public void send(MimeMessage... mimeMessages) throws MailException {

  }

  @Override
  public void send(SimpleMailMessage... simpleMessages) throws MailException {
    for (SimpleMailMessage message : simpleMessages) {
      log.info(message.getText());
    }
  }

}
