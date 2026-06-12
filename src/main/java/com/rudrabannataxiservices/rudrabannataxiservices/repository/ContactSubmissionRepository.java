package com.rudrabannataxiservices.rudrabannataxiservices.repository;

import com.rudrabannataxiservices.rudrabannataxiservices.model.ContactSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactSubmissionRepository extends JpaRepository<ContactSubmission, Long>,
        JpaSpecificationExecutor<ContactSubmission> {

    // Find by email with pagination
    Page<ContactSubmission> findByEmail(String email, Pageable pageable);

    // Find by phone with pagination
    Page<ContactSubmission> findByPhone(String phone, Pageable pageable);

    // Find by date range with pagination
    Page<ContactSubmission> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate,
                                                   Pageable pageable);

    // Recent submissions with pagination
    @Query("SELECT c FROM ContactSubmission c ORDER BY c.createdAt DESC")
    List<ContactSubmission> findRecentSubmissions(Pageable pageable);

    // Search with pagination
    @Query(value = "SELECT * FROM contact_submissions WHERE " +
            "LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(phone) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    Page<ContactSubmission> searchSubmissions(@Param("keyword") String keyword, Pageable pageable);
}