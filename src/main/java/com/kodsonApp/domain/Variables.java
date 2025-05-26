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
public class Variables {
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
    //private LocalDate loadingDate;
    private LocalDate dateReceived;
    private String customer;
    private String subCompany;
    private String month;
    private String product;
    private Double allowableLoss;
    private Double costPerLtr;
    private String wayBillNum;
    private String overNightAllowance;
    private String quantityDischarged;
    private String destination;
    private String distance;
    private Double rate;
    private Double amount;
    private Double fuel;
    private Double productLossLit;
    private Double shortage;
    private Double valueGh;
    private Double deductionAtSource;
    private Double paymentDue;
    private Double amountReceivable;
    private Double cashPaid;
    private Double actualLoss;
    private String status;
    private String employeeId;
}
