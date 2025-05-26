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

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Shortages {
    @Id
    @UuidGenerator
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    private String userName;
    private String brv;
    private String bvo;
    private LocalDate date;
    private String month;
    private String product;
    private Double allowableLoss;
    private Double costPerLtr;
    private String wayBillNum;
    private String overNightAllowance;
    private String destination;
    private Double productLossLit;
    private Double shortage;
    private Double valueGh;
    private Double deductionAtSource;
    private Double paymentDue;
    private Double amountReceivable;
    private Double cashPaid;
    private Double actualLoss;
    private String employeeId;
    private Double balance;
}
