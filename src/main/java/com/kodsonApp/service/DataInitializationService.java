package com.kodsonApp.service;

import com.kodsonApp.domain.Kodson;
import com.kodsonApp.enumuration.Role;
import com.kodsonApp.repository.KodsonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Profile("!test") // Don't run during tests
public class DataInitializationService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);

    @Autowired
    private KodsonRepository kodsonRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting data initialization check...");

        try {
            // Only create users if no users exist in the database
            long userCount = kodsonRepository.count();
            if (userCount == 0) {
                logger.info("No users found in database. Creating default admin users...");
                createDefaultUsers();
                logger.info("Default users created successfully");
            } else {
                logger.info("Database already contains {} user(s). Skipping user initialization.", userCount);
            }

            logger.info("Data initialization check completed");
        } catch (Exception e) {
            logger.error("Error during data initialization: {}", e.getMessage(), e);
        }
    }

    private void createDefaultUsers() {
        // Create Super Admin user
        createUser(
            "I.T",
            "iconmaxwellsowusu@gmail.com",
            "0040105715@Icon",
            Role.ROLE_SUPER_ADMIN
        );

        // Create Admin user
        createUser(
            "FAM",
            "admin@kodsonplusltd.com",
            "admin123",
            Role.ROLE_ADMIN
        );

        // Create General Manager user
        createUser(
            "GM",
            "gm@kodsonplusltd.com",
            "gm123",
            Role.ROLE_SUPER_ADMIN
        );
    }

    private void createUser(String username, String email, String password, Role role) {
        try {
            // Double-check that user doesn't exist (extra safety)
            Kodson existingUser = kodsonRepository.findUserByUsername(username);
            if (existingUser != null) {
                logger.warn("User '{}' already exists. Skipping creation.", username);
                return;
            }

            Kodson user = new Kodson();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role.name());
            user.setAuthorities(role.getAuthorities());
            user.setActive(true);
            user.setNotLocked(true);
            user.setEnabled(true);
            user.setAccountVerified(true);
            user.setJoinDate(new Date());
            user.setStatus("ACTIVE");

            kodsonRepository.save(user);
            logger.info("Successfully created user: {} with role: {}", username, role.name());

        } catch (Exception e) {
            logger.error("Failed to create user '{}': {}", username, e.getMessage(), e);
        }
    }
}
