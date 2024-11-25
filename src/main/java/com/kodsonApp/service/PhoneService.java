package com.kodsonApp.service;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PhoneService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    public void sendMessage(String employeeName, String payrollId, String phone) throws IOException {
        createMessage(employeeName, payrollId, phone);
    }

    private String createMessage(String employeeName, String payrollId, String phone) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\n" +
                "    \"key\": \"!ea48d_nrzi2lnz1u6emq#78tak#jlri(e5y2y763p(7o5xt#x63vnyajnlsz0ue\",\n" +
                "    \"msisdn\": \"" + phone + "\",\n" +
                "    \"message\": \"Hello " + employeeName + ",using your mobile data please click this link to access your pay slip: https://kodsonsystems.com:2080/SmsPaySlip?payrollId="+payrollId+"\",\n" +
                "    \"sender_id\": \"KODSON PLUS\"\n" +
                "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
        //System.out.println(", please click this link to access your pay slip: http://41.139.44.167:2443/SmsPaySlip?payrollId=" + payrollId );
        Request request = new Request.Builder()
                .url("https://sms.nalosolutions.com/smsbackend/Nal_resl/send-message/")
                .method("POST", body)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                System.out.println("Request successful. Response: " + response.body().string());
            } else {
                System.err.println("Request failed. Response code: " + response.code());
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return null;
    }
}
