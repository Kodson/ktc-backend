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
public class AccountStatement {
    @Id
    @UuidGenerator
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, updatable = false)

    private String id;
    private String station;
    private String date;
    private double superTotal;
    private double superRate;
    private double superAmount;
    private double agoTotal;
    private double agoRate;
    private double agoAmount;
    private double collection;
    private double deposit;
    private double others;
    private double totalGross;
    private double creditSales;
    private double expenses;
    private double cashDeposit;
    private double totalNet;
    private double amountBanked;
    private double overage;
    private double shortage;

}
