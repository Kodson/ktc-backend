package com.kodsonApp.utility;

public class EmailUtils {
    public static String getEmailMessage(String name, String host, String token) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click the link below to verify your account make sure you are using your mobile data if you are in the office. \n\n" +
                getVerificationUrl(host, token) + "\n\nThe support Team";
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "?token=" + token;
        //return host + "/kodson/user?token=" + token;
    }
}
