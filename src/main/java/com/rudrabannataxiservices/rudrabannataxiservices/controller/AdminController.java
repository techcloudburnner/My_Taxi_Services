package com.rudrabannataxiservices.rudrabannataxiservices.controller;

import com.rudrabannataxiservices.rudrabannataxiservices.Appconstant.AdminApiConstants;
import com.rudrabannataxiservices.rudrabannataxiservices.dto.AdminDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(AdminApiConstants.BASE_URL)
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // =========================================================
    // CREATE ADMIN
    // POST: /api/admins
    // =========================================================
    @PostMapping
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody AdminDTO adminDTO) {
        AdminDTO createdAdmin = adminService.createAdmin(adminDTO);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    // =========================================================
    // UPDATE ADMIN (Username & Password)
    // PUT: /api/admins/{id}
    // =========================================================
    @PutMapping(AdminApiConstants.BY_ID)
    public ResponseEntity<AdminDTO> updateAdmin(
            @PathVariable Long id,
            @RequestBody AdminDTO adminDTO) {
        AdminDTO updatedAdmin = adminService.updateAdmin(id, adminDTO);
        return ResponseEntity.ok(updatedAdmin);
    }

    // =========================================================
    // DELETE ADMIN
    // DELETE: /api/admins/{id}
    // =========================================================
    @DeleteMapping(AdminApiConstants.BY_ID)
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok(AdminApiConstants.ADMIN_DELETED);
    }

    // =========================================================
    // GET ADMIN BY ID
    // GET: /api/admins/{id}
    // =========================================================
    @GetMapping(AdminApiConstants.BY_ID)
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable Long id) {
        AdminDTO admin = adminService.getAdminById(id);
        return ResponseEntity.ok(admin);
    }

    // =========================================================
    // GET ALL ADMINS (Paginated)
    // GET: /api/admins?page=0&size=10&sort=createdAt,desc
    // =========================================================
    @GetMapping
    public ResponseEntity<Page<AdminDTO>> getAllAdmins(
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        Page<AdminDTO> admins = adminService.getAllAdmins(pageable);
        return ResponseEntity.ok(admins);
    }

    // =========================================================
    // LOGIN (Creates Session)
    // POST: /api/admins/login
    // =========================================================
    @PostMapping(AdminApiConstants.LOGIN)
    public ResponseEntity<AdminDTO> login(
            @RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        AdminDTO admin = adminService.login(username, password);
        return ResponseEntity.ok(admin);
    }

    // =========================================================
    // LOGOUT (Clears Session)
    // POST: /api/admins/{id}/logout
    // =========================================================
    @PostMapping(AdminApiConstants.LOGOUT)
    public ResponseEntity<String> logout(@PathVariable Long id) {
        adminService.logout(id);
        return ResponseEntity.ok(AdminApiConstants.LOGOUT_SUCCESS);
    }

    // =========================================================
    // VALIDATE SESSION
    // POST: /api/admins/validate-session
    // =========================================================
    @PostMapping("/validate-session")
    public ResponseEntity<AdminDTO> validateSession(
            @RequestBody Map<String, String> request) {
        String sessionToken = request.get("sessionToken");
        AdminDTO admin = adminService.validateSession(sessionToken);
        return ResponseEntity.ok(admin);
    }

    // =========================================================
    // CHECK USERNAME EXISTS
    // GET: /api/admins/check-username?username=admin
    // =========================================================
    @GetMapping(AdminApiConstants.CHECK_USERNAME)
    public ResponseEntity<Boolean> checkUsername(
            @RequestParam String username) {
        boolean exists = adminService.isUsernameExists(username);
        return ResponseEntity.ok(exists);
    }

    // =========================================================
    // GET TOTAL ADMINS COUNT
    // GET: /api/admins/count
    // =========================================================
    @GetMapping(AdminApiConstants.COUNT)
    public ResponseEntity<Long> getTotalAdmins() {
        long count = adminService.getTotalAdmins();
        return ResponseEntity.ok(count);
    }
}