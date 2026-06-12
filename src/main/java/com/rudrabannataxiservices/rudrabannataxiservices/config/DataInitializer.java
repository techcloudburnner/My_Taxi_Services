package com.rudrabannataxiservices.rudrabannataxiservices.config;

import com.rudrabannataxiservices.rudrabannataxiservices.model.Admin;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    // 🎯 Default Admin Credentials (Plain Text)
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    @Override
    public void run(String... args) {
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
        if (adminRepository.findByUsername(DEFAULT_USERNAME).isEmpty()) {

            Admin admin = new Admin();
            admin.setUsername(DEFAULT_USERNAME);
            admin.setPassword(DEFAULT_PASSWORD); // Plain text

            adminRepository.save(admin);

            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║     ✅ DEFAULT ADMIN CREATED           ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║  👤 Username: " + DEFAULT_USERNAME + "                  ║");
            System.out.println("║  🔑 Password: " + DEFAULT_PASSWORD + "              ║");
            System.out.println("╚════════════════════════════════════════╝");
        } else {
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║  ℹ️  Default admin already exists      ║");
            System.out.println("╚════════════════════════════════════════╝");
        }
    }
}