package com.rudrabannataxiservices.rudrabannataxiservices.repository;

import com.rudrabannataxiservices.rudrabannataxiservices.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Find admin by username
    Optional<Admin> findByUsername(String username);

    // Find admin by username and password (for login)
    Optional<Admin> findByUsernameAndPassword(String username, String password);

    // Find admin by session token
    Optional<Admin> findBySessionToken(String sessionToken);

    // Check if username exists
    boolean existsByUsername(String username);
}