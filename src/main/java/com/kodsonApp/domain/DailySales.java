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
public class DailySales {
    @Id
    @UuidGenerator
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, updatable = false)

    private String id;
    private String date;
    private String product;
    private double openSL;
    private double supply;
    private double overageShortageL;
    private double availableL;
    private double closingSL;
    private double differenceL;
    private double checkL;
    private double openSR;
    private double closingSR;
    private double returnTT;
    private double salesL;
    private double rate;
    private double value;
    private double cashSales;
    private double creditSales;
    private double advances;
    private double shortageMomo;
    private double cashAvailable;
    private double repaymentShortageMomo;
    private double repaymentAdvances;
    private double receivedFromDebtors;
    private double cashToBank;
    private double bankLodgement;
    private String station;
}
