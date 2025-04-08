package com.kodsonApp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
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
@Table(name = "productLifting")
public class ProductLifting {
    @Id
    @UuidGenerator
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, updatable = false)

    private String id;
    private String date;
    private String product;
    private String stations;
    private String brv;
    private String bdc;
    private Double rate;
    private Double uppf;
    private Double duty;
    private Double bostMargin;
    private Double npaMarking;
    private Double priceStabilization;
    private Double priceDistribution;
    private Double lpgCompensation;
    private Double qty;
    private Double price;
    private String wayBill;

}
