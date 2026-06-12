package com.rudrabannataxiservices.rudrabannataxiservices.service.impl;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.AdminDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.model.Admin;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.AdminRepository;
import com.rudrabannataxiservices.rudrabannataxiservices.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // =========================================================
    // CREATE - Plain password, updatedAt NULL
    // =========================================================
    @Override
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        if (adminRepository.existsByUsername(adminDTO.getUsername())) {
            throw new RuntimeException("Username already exists: " + adminDTO.getUsername());
        }

        Admin admin = new Admin();
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(adminDTO.getPassword()); // Plain text password

        Admin savedAdmin = adminRepository.save(admin);
        return convertToDTO(savedAdmin);
    }

    // =========================================================
    // UPDATE - Plain password update
    // =========================================================
    @Override
    public AdminDTO updateAdmin(Long id, AdminDTO adminDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));

        // Update username
        if (adminDTO.getUsername() != null && !adminDTO.getUsername().trim().isEmpty()) {
            if (!admin.getUsername().equals(adminDTO.getUsername()) &&
                    adminRepository.existsByUsername(adminDTO.getUsername())) {
                throw new RuntimeException("Username already taken: " + adminDTO.getUsername());
            }
            admin.setUsername(adminDTO.getUsername());
        }

        // Update password (Plain text)
        if (adminDTO.getPassword() != null && !adminDTO.getPassword().trim().isEmpty()) {
            admin.setPassword(adminDTO.getPassword()); // Plain text password
        }

        Admin updatedAdmin = adminRepository.save(admin);
        return convertToDTO(updatedAdmin);
    }

    // =========================================================
    // DELETE
    // =========================================================
    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    // =========================================================
    // GET BY ID
    // =========================================================
    @Override
    public AdminDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
        return convertToDTO(admin);
    }

    // =========================================================
    // GET ALL (Paginated)
    // =========================================================
    @Override
    public Page<AdminDTO> getAllAdmins(Pageable pageable) {
        Page<Admin> admins = adminRepository.findAll(pageable);
        return admins.map(this::convertToDTO);
    }

    // =========================================================
    // LOGIN - Direct string comparison
    // =========================================================
    @Override
    public AdminDTO login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Direct string comparison
        if (!password.equals(admin.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate session token
        String sessionToken = UUID.randomUUID().toString();
        admin.setSessionToken(sessionToken);
        admin.setLastLogin(LocalDateTime.now());

        adminRepository.save(admin);

        AdminDTO dto = convertToDTO(admin);
        dto.setSessionToken(sessionToken);
        return dto;
    }

    // =========================================================
    // LOGOUT
    // =========================================================
    @Override
    public void logout(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));

        admin.setSessionToken(null);
        adminRepository.save(admin);
    }

    // =========================================================
    // VALIDATE SESSION
    // =========================================================
    @Override
    public AdminDTO validateSession(String sessionToken) {
        Admin admin = adminRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new RuntimeException("Invalid or expired session"));
        return convertToDTO(admin);
    }

    // =========================================================
    // CHECK USERNAME
    // =========================================================
    @Override
    public boolean isUsernameExists(String username) {
        return adminRepository.existsByUsername(username);
    }

    // =========================================================
    // COUNT
    // =========================================================
    @Override
    public long getTotalAdmins() {
        return adminRepository.count();
    }

    // =========================================================
    // HELPER: Entity to DTO
    // =========================================================
    private AdminDTO convertToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setUsername(admin.getUsername());
        dto.setPassword(null); // Never send password
        dto.setLastLogin(admin.getLastLogin());
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setUpdatedAt(admin.getUpdatedAt());
        return dto;
    }
}