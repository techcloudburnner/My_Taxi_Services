package com.rudrabannataxiservices.rudrabannataxiservices.service;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.AdminDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    AdminDTO createAdmin(AdminDTO adminDTO);
    AdminDTO updateAdmin(Long id, AdminDTO adminDTO);
    void deleteAdmin(Long id);

    AdminDTO getAdminById(Long id);
    Page<AdminDTO> getAllAdmins(Pageable pageable);

    AdminDTO login(String username, String password);
    void logout(Long id);
    AdminDTO validateSession(String sessionToken);

    boolean isUsernameExists(String username);
    long getTotalAdmins();
}