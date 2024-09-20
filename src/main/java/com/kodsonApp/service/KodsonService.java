package com.kodsonApp.service;

import com.kodsonApp.domain.Kodson;
import com.kodsonApp.exception.domain.*;
import org.springframework.messaging.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface KodsonService {
    Kodson register(String phone, String username, String email, String password) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, javax.mail.MessagingException, IOException;

    List<Kodson> getUsers();

    Kodson findUserByUsername(String username);

    String findEmailByUsername(String username);

    Kodson findUserByEmail(String email);
    Kodson sendLink(String email) throws EmailNotFoundException;
    Kodson findUserByPhone(String phone);
    Kodson addNewUser(String password,String phone, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    Kodson updateUser(String phone, String currentUsername, String newBranch, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException;

    void deleteUser(String username) throws IOException;

    void resetPassword(String mail, String password) throws MessagingException, EmailNotFoundException, javax.mail.MessagingException, IOException;
    Boolean verifyToken(String token);

    Boolean verifyOtp(String username);


    Boolean deVerifyOtp(String username);
    Kodson updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException, NotAnImageFileException;



}
