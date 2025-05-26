package com.kodsonApp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Employees {
    @Id
    @UuidGenerator
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    private String name;
    private String gender;
    private String maritalStatus;
    private String email;
    private String tel;
    private String address;
    private String ssnit;
    private String companyName;
    private String department;
    private String position;
    private String dateJoining;
    private String dateLeaving;
    private String status;
    private double basicSalary;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private String branch;
    private String sortCode;
    private Boolean paye;
    private Boolean snit;
}
