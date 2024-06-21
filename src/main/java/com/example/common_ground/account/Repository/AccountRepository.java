package com.example.common_ground.account.Repository;

import com.example.common_ground.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

//특정 스레드가 데이터를 쓰는 동안에는 다른 스레드가 해당 데이터를 읽거나 쓸 수 없도록 막는다.
//즉, 쓰기 연산이 끝날 때까지 다른 스레드는 대기해야 한다.
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsByEmail(String email);
  boolean existsByNickname(String nickname);
}
