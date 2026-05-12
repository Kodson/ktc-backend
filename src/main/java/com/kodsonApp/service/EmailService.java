package com.kodsonApp.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);

    void sendOtpMail(String to, String otp);

    void sendResetLink(String to);
}
