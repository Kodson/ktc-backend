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
public class PayRoll {
    @Id
    @UuidGenerator
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    private String employeeName;
    private String ssnit;
    private String position;
    private String payrollDate;
    private String department;
    private String month;
    private String companyName;
    private double basicSalary;
    private double employeeSSF;
    private double taxableSalary;
    private double payee;
    //private double debtBalance;
    private double loan_Surcharge;
    private double adjustments;
    private double deductions;
    private double bicycle;
    //private double employee5;
    private double employerSSF;
    private double statutoryDeductions;
    private double netPayBeforeDeductions;
    private double debtBalBD;
    private double balanceOutstanding;
    private double totalDeduction;
    private double netSalaryPayable;
    private String paymentMethod;
    private String bankBranch;
    private String accountNumber;
    private double paidAmount;
    private String employeeId;
    private String currentLoan;
    private String currentSurcharge;
}