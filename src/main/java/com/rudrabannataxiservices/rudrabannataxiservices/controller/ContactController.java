package com.rudrabannataxiservices.rudrabannataxiservices.controller;

import com.rudrabannataxiservices.rudrabannataxiservices.Appconstant.ContactApiConstants;
import com.rudrabannataxiservices.rudrabannataxiservices.dto.ContactSubmissionDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(ContactApiConstants.BASE_URL)
@CrossOrigin(origins = "*")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Create Contact
    @PostMapping
    public ResponseEntity<ContactSubmissionDTO> createContact(
            @RequestBody ContactSubmissionDTO contactDTO) {

        ContactSubmissionDTO createdContact =
                contactService.createContact(contactDTO);

        return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
    }

    // Get All Contacts
    @GetMapping
    public ResponseEntity<Page<ContactSubmissionDTO>> getAllContacts(
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        Page<ContactSubmissionDTO> contacts =
                contactService.getAllContacts(pageable);

        return ResponseEntity.ok(contacts);
    }

    // Get Contact By ID
    @GetMapping(ContactApiConstants.BY_ID)
    public ResponseEntity<ContactSubmissionDTO> getContactById(
            @PathVariable Long id) {

        ContactSubmissionDTO contact =
                contactService.getContactById(id);

        return ResponseEntity.ok(contact);
    }

    // Filter Contacts
    @GetMapping(ContactApiConstants.FILTER)
    public ResponseEntity<Page<ContactSubmissionDTO>> getFilteredContacts(

            @RequestParam(required = false) String name,

            @RequestParam(required = false) String email,

            @RequestParam(required = false) String phone,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,

            @PageableDefault(size = 10)
            Pageable pageable) {

        Page<ContactSubmissionDTO> contacts =
                contactService.getFilteredContacts(
                        name,
                        email,
                        phone,
                        startDate,
                        endDate,
                        pageable
                );

        return ResponseEntity.ok(contacts);
    }

    // Search Contacts
    @GetMapping(ContactApiConstants.SEARCH)
    public ResponseEntity<Page<ContactSubmissionDTO>> searchContacts(

            @RequestParam String keyword,

            @PageableDefault(size = 10)
            Pageable pageable) {

        Page<ContactSubmissionDTO> contacts =
                contactService.searchContacts(keyword, pageable);

        return ResponseEntity.ok(contacts);
    }

    // Delete Contact
    @DeleteMapping(ContactApiConstants.BY_ID)
    public ResponseEntity<String> deleteContact(
            @PathVariable Long id) {

        contactService.deleteContact(id);

        return ResponseEntity.ok(
                ContactApiConstants.CONTACT_DELETED
        );
    }

    // Get Recent Contacts
    @GetMapping(ContactApiConstants.RECENT)
    public ResponseEntity<List<ContactSubmissionDTO>> getRecentContacts(
            @RequestParam(defaultValue = "5") int limit) {

        List<ContactSubmissionDTO> contacts =
                contactService.getRecentContacts(limit);

        return ResponseEntity.ok(contacts);
    }

    // Get Total Contacts Count
    @GetMapping(ContactApiConstants.COUNT)
    public ResponseEntity<Long> getTotalContacts() {

        long count = contactService.getTotalContacts();

        return ResponseEntity.ok(count);
    }
}
