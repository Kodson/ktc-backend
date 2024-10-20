package com.kodsonApp.service.impl;

import com.google.common.cache.LoadingCache;
import com.kodsonApp.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import static com.kodsonApp.utility.EmailUtils.getEmailMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String OTP = "New otp";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "email-template";
    public static final String TEXT_HTML_ENCONDING = "text/html";
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private LoadingCache<String, String> mailCache;



    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom("info@kodsonplusltd.com");
            message.setTo(to);
            message.setText(getEmailMessage(name, "https://kodsonsystems.com:2443/ActivateAccount", token));
            //message.setText(getEmailMessage(name, "http://localhost:8081", token));
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }

    }

    @Override
    public void sendOtpMail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(OTP);
            message.setFrom("info@kodsonplusltd.com");
            message.setTo(to);
            message.setText("Your one time password is :"+ otp +"\n this expires in 5 minutes");
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void sendResetLink(String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Password Reset Link");
            message.setFrom("info@kodsonplusltd.com");
            message.setTo(to);
            message.setText("https://kodsonsystems.com:2443/resetpassword");
            emailSender.send(message);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String filename) {
        return "<" + filename + ">";
    }
}