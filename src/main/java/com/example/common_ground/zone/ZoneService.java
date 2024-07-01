package com.example.common_ground.zone;

import com.example.common_ground.account.entity.Zone;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ZoneService {

  private final ZoneRepository zoneRepository;

  // 해당 메서드가 객체 생성 후에 초기화될 때 실행되어야 함을 표시
  @PostConstruct
  public void initZoneDate() throws IOException {
    // 데이터가 하나도 없다면, 데이터를 추가한다
    if (zoneRepository.count() == 0) {
      // Resource: springframework.core
      Resource resource = new ClassPathResource("zones_kr.csv");
      // zones_kr.csv에 있는 데이터를 전부 객체로 읽어온다
      List<Zone> zoneList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream()
              .map(line -> {
                String[] split = line.split(","); // 각각의 줄을 ","기준으로 나눠주기
                return Zone.builder()
                        .city(split[0]) // ex)Andong
                        .localNameOfCity(split[1]) // ex)안동시
                        .province(split[2])
                        .build(); // ex)North Gyeongsang (경상 북도)
              }).collect(Collectors.toList());// 콜렉트를 사용하여 리스트로 받기
      zoneRepository.saveAll(zoneList);
    }
  }

}
