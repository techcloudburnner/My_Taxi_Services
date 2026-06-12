package com.rudrabannataxiservices.rudrabannataxiservices.service.impl;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.ContactSubmissionDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.model.ContactSubmission;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.ContactSubmissionRepository;
import com.rudrabannataxiservices.rudrabannataxiservices.service.ContactService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactSubmissionRepository contactRepository;

    @Override
    public ContactSubmissionDTO createContact(ContactSubmissionDTO contactDTO) {
        // Validate
        if (contactDTO.getName() == null || contactDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (contactDTO.getPhone() == null || contactDTO.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Phone is required");
        }
        if (contactDTO.getMessage() == null || contactDTO.getMessage().trim().isEmpty()) {
            throw new RuntimeException("Message is required");
        }

        // Convert DTO to Entity
        ContactSubmission contact = new ContactSubmission();
        contact.setName(contactDTO.getName());
        contact.setPhone(contactDTO.getPhone());
        contact.setEmail(contactDTO.getEmail());
        contact.setMessage(contactDTO.getMessage());

        // Save to database
        ContactSubmission savedContact = contactRepository.save(contact);

        // Convert Entity to DTO and return
        return convertToDTO(savedContact);
    }

    @Override
    public Page<ContactSubmissionDTO> getAllContacts(Pageable pageable) {
        Page<ContactSubmission> contacts = contactRepository.findAll(pageable);
        return contacts.map(this::convertToDTO);
    }

    @Override
    public ContactSubmissionDTO getContactById(Long id) {
        ContactSubmission contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        return convertToDTO(contact);
    }

    @Override
    public Page<ContactSubmissionDTO> getFilteredContacts(String name, String email, String phone,
                                                          LocalDateTime startDate, LocalDateTime endDate,
                                                          Pageable pageable) {

        // Dynamic filtering using Specification
        Specification<ContactSubmission> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by name (partial match, case insensitive)
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                ));
            }

            // Filter by email (partial match, case insensitive)
            if (email != null && !email.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"
                ));
            }

            // Filter by phone (partial match)
            if (phone != null && !phone.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        root.get("phone"),
                        "%" + phone + "%"
                ));
            }

            // Filter by date range
            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("createdAt"), startDate, endDate
                ));
            } else if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"), startDate
                ));
            } else if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"), endDate
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<ContactSubmission> contacts = contactRepository.findAll(spec, pageable);
        return contacts.map(this::convertToDTO);
    }

    @Override
    public Page<ContactSubmissionDTO> searchContacts(String keyword, Pageable pageable) {
        Page<ContactSubmission> contacts = contactRepository.searchSubmissions(keyword, pageable);
        return contacts.map(this::convertToDTO);
    }

    @Override
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("Contact not found with id: " + id);
        }
        contactRepository.deleteById(id);
    }

    @Override
    public long getTotalContacts() {
        return contactRepository.count();
    }

    @Override
    public List<ContactSubmissionDTO> getRecentContacts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ContactSubmission> contacts = contactRepository.findRecentSubmissions(pageable);
        return contacts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper method: Entity to DTO conversion
    private ContactSubmissionDTO convertToDTO(ContactSubmission contact) {
        ContactSubmissionDTO dto = new ContactSubmissionDTO();
        dto.setId(contact.getId());
        dto.setName(contact.getName());
        dto.setPhone(contact.getPhone());
        dto.setEmail(contact.getEmail());
        dto.setMessage(contact.getMessage());
        dto.setCreatedAt(contact.getCreatedAt());
        return dto;
    }
}