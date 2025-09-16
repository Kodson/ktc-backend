package com.kodsonApp.service;

import com.kodsonApp.DTO.StationDTO;
import com.kodsonApp.domain.Kodson;
import com.kodsonApp.domain.Stations;
import com.kodsonApp.repository.KodsonRepository;
import com.kodsonApp.repository.StationsRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor

public class StationsService {
    @Autowired
    private final StationsRepo stationsRepo;
    private final KodsonRepository kodsonRepo;

    public Page<StationDTO> getAllStations(int page, int size) {
        return stationsRepo.findAll(PageRequest.of(page, size))
                .map(this::mapToDTO);
    }

    private StationDTO mapToDTO(Stations station) {
        StationDTO.User userDTO = null;

        if (station.getManagerUserId() != null) {
            try {
                Kodson user = kodsonRepo.findById(Long.valueOf(station.getManagerUserId()))
                        .orElse(null);

                if (user != null) {
                    userDTO = StationDTO.User.builder()
                            .id(user.getId().toString())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .status(station.getManagerStatus())
                            .build();
                }
            } catch (NumberFormatException ex) {
                log.warn("Invalid managerUserId {} for station {}", station.getManagerUserId(), station.getId());
            }
        }

        StationDTO.Contact.ContactBuilder contactBuilder = StationDTO.Contact.builder()
                .phone(station.getPhone())
                .email(station.getEmail());

        log.info("Manager field: {}", station.getManager());

// check both null and empty
        if (station.getManager() != null && !station.getManager().isBlank()) {
            contactBuilder.manager(
                    StationDTO.Contact.Manager.builder()
                            .name(station.getManager())
                            .phone(station.getManagerPhone())
                            .email(station.getManagerEmail())
                            .userId(station.getManagerUserId())
                            .build()
            );
        }

        StationDTO.Contact contact = contactBuilder.build();


        return StationDTO.builder()
                .id(station.getId())
                .name(station.getName())
                .code(station.getCode())
                .location(
                        StationDTO.Location.builder()
                                .address(station.getAddress())
                                .city(station.getCity())
                                .region(station.getRegion())
                                .build()
                )
                .contact(contact) // ✅ safe contact with optional manager
                .operational(
                        StationDTO.Operational.builder()
                                .status(station.getStatus())
                                .operatingHours(station.getOperatingHours())
                                .fuelTypes(station.getFuelTypes())
                                .tankCapacity(station.getTankCapacity())
                                .pumpCount(station.getPumpCount())
                                .build()
                )
                .financial(
                        StationDTO.Financial.builder()
                                .monthlyTarget(station.getMonthlyTarget())
                                .build()
                )
                .user(userDTO)
                .build();
    }


    public Stations getStation(String id) {
        return stationsRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Stations createBdc(Stations station) {
        return stationsRepo.save(station);
    }

    /*public Stations assignManager(String stationId, String managerId) {
        Stations station = getStation(stationId);
        station.setManager(managerId);
        return stationsRepo.save(station);
    }*/

    public Stations assignManager(String stationId, Long managerUserId) {
        Stations station = getStation(stationId);
        // Fetch manager from Kodson entity/repo
        Kodson manager = kodsonRepo.findById(managerUserId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        // Copy data into station
        station.setManager(manager.getUsername());
        station.setManagerPhone(manager.getPhone());
        station.setManagerEmail(manager.getEmail());
        station.setManagerUserId(manager.getId().toString());
        station.setManagerStatus("ACTIVE");
        station.setStatus("ACTIVE");
        return stationsRepo.save(station);
    }

    public Stations findStationByUsername(String username) {
        // Implement the logic to fetch station by username
        // For example, assuming the repository has a method findByManager
        return stationsRepo.findByManager(username).orElseThrow(() -> new RuntimeException("Station not found"));
    }

    public Stations findStationByManagerUserId(String managerUserId) {
        return stationsRepo.findByManagerUserId(managerUserId)
                .orElse(null); // Return null if no station is found for this manager
    }

    public void deleteExpense(String id) {
        stationsRepo.deleteById(id);
    }

    public Stations unassignManager(String stationId) {
        Stations station = getStation(stationId);
        station.setManager(null);
        station.setManagerPhone(null);
        station.setManagerEmail(null);
        station.setManagerUserId(null);
        station.setManagerStatus("INACTIVE");
        return stationsRepo.save(station);
    }
}
