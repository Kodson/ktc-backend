
package com.kodsonApp.repository;

import com.kodsonApp.domain.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationsRepo extends JpaRepository<Locations, String> {
}
