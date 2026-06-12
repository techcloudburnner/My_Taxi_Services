package com.rudrabannataxiservices.rudrabannataxiservices.service;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.ContactSubmissionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface ContactService {

    // Create contact submission
    ContactSubmissionDTO createContact(ContactSubmissionDTO contactDTO);

    // Get all contacts with pagination
    Page<ContactSubmissionDTO> getAllContacts(Pageable pageable);

    // Get contact by ID
    ContactSubmissionDTO getContactById(Long id);

    // Get contacts with filtering and pagination
    Page<ContactSubmissionDTO> getFilteredContacts(String name, String email, String phone,
                                                   LocalDateTime startDate, LocalDateTime endDate,
                                                   Pageable pageable);

    // Search contacts by keyword
    Page<ContactSubmissionDTO> searchContacts(String keyword, Pageable pageable);

    // Delete contact
    void deleteContact(Long id);

    // Count total contacts
    long getTotalContacts();

    // Get recent contacts
    List<ContactSubmissionDTO> getRecentContacts(int limit);
}