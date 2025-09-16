package com.kodsonApp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@ToString
public class Stations {

    @Id
    @UuidGenerator
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, updatable = false)
    private String id;

    private String name;
    private String code;
    private String address;
    private String city;
    private String region;
    private String phone;
    private String email;
    private String status;
    private String managerStatus;

    @ElementCollection
    @CollectionTable(name = "station_operating_hours", joinColumns = @JoinColumn(name = "station_id"))
    @MapKeyColumn(name = "day")
    @Column(name = "hours")
    private Map<String, String> operatingHours;

    @ElementCollection
    @CollectionTable(name = "station_fuel_types", joinColumns = @JoinColumn(name = "station_id"))
    @Column(name = "fuel_type")
    private List<String> fuelTypes;

    @ElementCollection
    @CollectionTable(name = "station_tank_capacity", joinColumns = @JoinColumn(name = "station_id"))
    @MapKeyColumn(name = "fuel_type")
    @Column(name = "capacity")
    private Map<String, Integer> tankCapacity;

    private int pumpCount;
    private double monthlyTarget;

    private String manager;
    private String managerPhone;
    private String managerEmail;
    private String managerUserId;
}
