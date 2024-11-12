package com.kodsonApp.utility;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PettyCashSms {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    public void sendStation(double amount, String request, String phone) throws IOException {
        createStation(amount, request,  phone);
    }

    public void sendGm(double amount, String sender) throws IOException {
        createGm(amount, sender);
    }

    public void sendGmItem(String item) throws IOException {
        createGmItem(item);
    }

    public void sendManager(double amount, String phone) throws IOException {
        createManager(amount, phone);
    }

    public void sendManagerItem(String item, String phone) throws IOException {
        createManagerItem(item, phone);
    }

    public void sendTask(String phone, String description) throws IOException {
        createTask(phone, description);
    }

    private String createTask(String phone, String description) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\n" +
                "    \"key\": \"!ea48d_nrzi2lnz1u6emq#78tak#jlri(e5y2y763p(7o5xt#x63vnyajnlsz0ue\",\n" +
                "    \"msisdn\": \"" + phone + "\",\n" +
                "    \"message\": \" Your task with description :"+ description + ", is due in one day. "+"\",\n" +
                "    \"sender_id\": \"KODSON PLUS\"\n" +
                "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
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

    private String createManagerItem(String item, String phone) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\n" +
                "    \"key\": \"!ea48d_nrzi2lnz1u6emq#78tak#jlri(e5y2y763p(7o5xt#x63vnyajnlsz0ue\",\n" +
                "    \"msisdn\": \"" + phone + "\",\n" +
                "    \"message\": \""+ item + ", has been approved "+"\",\n" +
                "    \"sender_id\": \"KODSON PLUS\"\n" +
                "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
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

    public String sendOtp(String otp, String phone) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\n" +
                "    \"key\": \"!ea48d_nrzi2lnz1u6emq#78tak#jlri(e5y2y763p(7o5xt#x63vnyajnlsz0ue\",\n" +
                "    \"msisdn\": \"" + phone + "\",\n" +
                "    \"message\": \""+ otp + " is you otp,"+"\",\n" +
                "    \"sender_id\": \"KODSON PLUS\"\n" +
                "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
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

    private String createManager(double amount, String phone) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\n" +
                "    \"key\": \"!ea48d_nrzi2lnz1u6emq#78tak#jlri(e5y2y763p(7o5xt#x63vnyajnlsz0ue\",\n" +
                "    \"msisdn\": \"" + phone + "\",\n" +
                "    \"message\": \""+ amount + ", has been approved "+"\",\n" +
                "    \"sender_id\": \"KODSON PLUS\"\n" +
                "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
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

    private String createGmItem(String item){
        OkHttpClient client = new OkHttpClient();
        String phone = "0501577846";
        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\n" +
                "    \"key\": \"!ea48d_nrzi2lnz1u6emq#78tak#jlri(e5y2y763p(7o5xt#x63vnyajnlsz0ue\",\n" +
                "    \"msisdn\": \"" + phone + "\",\n" +
                "    \"message\": \"Request for maintenance item : "+ item + " https://kodsonsystems.com:2080 "+"\",\n" +
                "    \"sender_id\": \"KODSON PLUS\"\n" +
                "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
        //System.out.println(", please click this link to access your pay slip: http://localhost:3000/SmsPaySlip?payrollId=" + phone );
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

    private String createGm(double amount, String sender){
        OkHttpClient client = new OkHttpClient();
        String phone = "0501577846";
        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\n" +
                "    \"key\": \"!ea48d_nrzi2lnz1u6emq#78tak#jlri(e5y2y763p(7o5xt#x63vnyajnlsz0ue\",\n" +
                "    \"msisdn\": \"" + phone + "\",\n" +
                "    \"message\": \"Request from "+sender +" amount: "+ amount + "PLEASE KINDLY MAKE SURE YOU ARE USING YOUR MOBILE DATA https://kodsonsystems.com:2080"+"\",\n" +
                "    \"sender_id\": \"KODSON PLUS\"\n" +
                "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
        //System.out.println(", please click this link to access your pay slip: http://localhost:3000/SmsPaySlip?payrollId=" + phone );
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


    private String createStation(double amount, String requestD, String phone) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\n" +
                "    \"key\": \"!ea48d_nrzi2lnz1u6emq#78tak#jlri(e5y2y763p(7o5xt#x63vnyajnlsz0ue\",\n" +
                "    \"msisdn\": \"" + phone + "\",\n" +
                "    \"message\": \""+requestD +" : "+ amount + ", has been approved "+"\",\n" +
                "    \"sender_id\": \"KODSON PLUS\"\n" +
                "}";

        RequestBody body = RequestBody.create(mediaType, requestBody);
        //System.out.println(", please click this link to access your pay slip: http://localhost:3000/SmsPaySlip?payrollId=" + phone );
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