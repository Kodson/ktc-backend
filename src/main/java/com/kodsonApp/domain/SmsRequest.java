package com.kodsonApp.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsRequest {
    private String employeeName;
    private String payrollId;
    private String tel;

    public SmsRequest() {}

    public SmsRequest(String employeeName, String payrollId, String tel) {
        this.employeeName = employeeName;
        this.payrollId = payrollId;
        this.tel = tel;
    }
}
