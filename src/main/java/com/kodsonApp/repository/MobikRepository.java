package com.kodsonApp.repository;

import com.kodsonApp.domain.Mobik;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MobikRepository extends JpaRepository<Mobik, Long> {
    Mobik findUserByUsername(String username);

    Mobik findUserByEmail(String email);

    Mobik findUserByPhone(String phone);

    Mobik findEmailByUsername(String username);

}
