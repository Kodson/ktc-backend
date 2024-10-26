package com.kodsonApp.repository;

import com.kodsonApp.domain.Kodson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KodsonRepository extends JpaRepository<Kodson, Long> {
    Kodson findUserByUsername(String username);

    Kodson findUserByEmail(String email);

    Kodson findUserByPhone(String phone);

    Kodson findEmailByUsername(String username);

    Kodson findPhoneByUsername(String username);

}
