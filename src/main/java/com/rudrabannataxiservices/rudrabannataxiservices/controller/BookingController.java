package com.rudrabannataxiservices.rudrabannataxiservices.controller;

import com.rudrabannataxiservices.rudrabannataxiservices.Appconstant.BookingApiConstants;
import com.rudrabannataxiservices.rudrabannataxiservices.dto.BookingDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(BookingApiConstants.BASE_URL)
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Create Booking
    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {

        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);

        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    // Update Booking
    @PutMapping(BookingApiConstants.BY_ID)
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingDTO bookingDTO) {

        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);

        return ResponseEntity.ok(updatedBooking);
    }

    // Delete Booking
    @DeleteMapping(BookingApiConstants.BY_ID)
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {

        bookingService.deleteBooking(id);

        return ResponseEntity.ok(BookingApiConstants.BOOKING_DELETED);
    }

    // Get Booking By ID
    @GetMapping(BookingApiConstants.BY_ID)
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {

        BookingDTO booking = bookingService.getBookingById(id);

        return ResponseEntity.ok(booking);
    }

    // Get Booking By Number
    @GetMapping(BookingApiConstants.BY_BOOKING_NUMBER)
    public ResponseEntity<BookingDTO> getBookingByNumber(
            @PathVariable String bookingNumber) {

        BookingDTO booking = bookingService.getBookingByNumber(bookingNumber);

        return ResponseEntity.ok(booking);
    }

    // Get All Bookings
    @GetMapping
    public ResponseEntity<Page<BookingDTO>> getAllBookings(
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        Page<BookingDTO> bookings = bookingService.getAllBookings(pageable);

        System.out.println("Total Bookings Found: " + bookings.getTotalElements());

        return ResponseEntity.ok(bookings);
    }

    // Filter Bookings
    @GetMapping(BookingApiConstants.FILTER)
    public ResponseEntity<Page<BookingDTO>> getFilteredBookings(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String customerPhone,
            @RequestParam(required = false) String customerEmail,
            @RequestParam(required = false) Long carId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate pickupDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @PageableDefault(size = 10) Pageable pageable) {

        Page<BookingDTO> bookings = bookingService.getFilteredBookings(
                customerName,
                customerPhone,
                customerEmail,
                carId,
                pickupDate,
                startDate,
                endDate,
                pageable
        );

        return ResponseEntity.ok(bookings);
    }

    // Search Bookings
    @GetMapping(BookingApiConstants.SEARCH)
    public ResponseEntity<Page<BookingDTO>> searchBookings(
            @RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<BookingDTO> bookings =
                bookingService.searchBookingsByCustomer(keyword, pageable);

        return ResponseEntity.ok(bookings);
    }

    // Get Bookings By Car
    @GetMapping(BookingApiConstants.BY_CAR)
    public ResponseEntity<List<BookingDTO>> getBookingsByCar(
            @PathVariable Long carId) {

        List<BookingDTO> bookings = bookingService.getBookingsByCar(carId);

        return ResponseEntity.ok(bookings);
    }

    // Get Bookings By Date
    @GetMapping(BookingApiConstants.BY_DATE)
    public ResponseEntity<List<BookingDTO>> getBookingsByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        List<BookingDTO> bookings = bookingService.getBookingsByDate(date);

        return ResponseEntity.ok(bookings);
    }

    // Get Bookings By Date Range
    @GetMapping(BookingApiConstants.DATE_RANGE)
    public ResponseEntity<List<BookingDTO>> getBookingsByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        List<BookingDTO> bookings =
                bookingService.getBookingsByDateRange(startDate, endDate);

        return ResponseEntity.ok(bookings);
    }

    // Get Recent Bookings
    @GetMapping(BookingApiConstants.RECENT)
    public ResponseEntity<List<BookingDTO>> getRecentBookings(
            @RequestParam(defaultValue = "5") int limit) {

        List<BookingDTO> bookings = bookingService.getRecentBookings(limit);

        return ResponseEntity.ok(bookings);
    }

    // Get Total Bookings Count
    @GetMapping(BookingApiConstants.COUNT)
    public ResponseEntity<Long> getTotalBookings() {

        long count = bookingService.getTotalBookings();

        return ResponseEntity.ok(count);
    }

    // Get Booking Count By Date
    @GetMapping(BookingApiConstants.COUNT_BY_DATE)
    public ResponseEntity<Long> getBookingCountByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        long count = bookingService.getBookingCountByDate(date);

        return ResponseEntity.ok(count);
    }
}
//package com.rudrabannataxiservices.rudrabannataxiservices.controller;
//
//import com.rudrabannataxiservices.rudrabannataxiservices.dto.BookingDTO;
//import com.rudrabannataxiservices.rudrabannataxiservices.service.BookingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/bookings")
//@CrossOrigin(origins = "*")
//public class BookingController {
//
//    @Autowired
//    private BookingService bookingService;
//
//    // Create Booking
//    @PostMapping
//    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
//        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
//        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
//    }
//
//    // Update Booking
//    @PutMapping("/{id}")
//    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO) {
//        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
//        return ResponseEntity.ok(updatedBooking);
//    }
//
//    // Delete Booking
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
//        bookingService.deleteBooking(id);
//        return ResponseEntity.ok("Booking deleted successfully");
//    }
//
//    // Get Booking by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
//        BookingDTO booking = bookingService.getBookingById(id);
//        return ResponseEntity.ok(booking);
//    }
//
//    // Get Booking by Booking Number
//    @GetMapping("/number/{bookingNumber}")
//    public ResponseEntity<BookingDTO> getBookingByNumber(@PathVariable String bookingNumber) {
//        BookingDTO booking = bookingService.getBookingByNumber(bookingNumber);
//        return ResponseEntity.ok(booking);
//    }
//
//    // Get All Bookings with Pagination
//    @GetMapping
//    public ResponseEntity<Page<BookingDTO>> getAllBookings(
//            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
//
//        Page<BookingDTO> bookings = bookingService.getAllBookings(pageable);
//
//        // FIX: Add this to debug
//        System.out.println("Total Bookings Found: " + bookings.getTotalElements());
//
//        return ResponseEntity.ok(bookings);
//    }
//
//    // Get Filtered Bookings with Pagination
//    @GetMapping("/filter")
//    public ResponseEntity<Page<BookingDTO>> getFilteredBookings(
//            @RequestParam(required = false) String customerName,
//            @RequestParam(required = false) String customerPhone,
//            @RequestParam(required = false) String customerEmail,
//            @RequestParam(required = false) Long carId,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate pickupDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            @PageableDefault(size = 10) Pageable pageable) {
//        Page<BookingDTO> bookings = bookingService.getFilteredBookings(
//                customerName, customerPhone, customerEmail, carId,
//                pickupDate, startDate, endDate, pageable);
//        return ResponseEntity.ok(bookings);
//    }
//
//    // Search Bookings by Customer Name
//    @GetMapping("/search")
//    public ResponseEntity<Page<BookingDTO>> searchBookings(
//            @RequestParam String keyword,
//            @PageableDefault(size = 10) Pageable pageable) {
//        Page<BookingDTO> bookings = bookingService.searchBookingsByCustomer(keyword, pageable);
//        return ResponseEntity.ok(bookings);
//    }
//
//    // Get Bookings by Car
//    @GetMapping("/car/{carId}")
//    public ResponseEntity<List<BookingDTO>> getBookingsByCar(@PathVariable Long carId) {
//        List<BookingDTO> bookings = bookingService.getBookingsByCar(carId);
//        return ResponseEntity.ok(bookings);
//    }
//
//    // Get Bookings by Date
//    @GetMapping("/date/{date}")
//    public ResponseEntity<List<BookingDTO>> getBookingsByDate(
//            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        List<BookingDTO> bookings = bookingService.getBookingsByDate(date);
//        return ResponseEntity.ok(bookings);
//    }
//
//    // Get Bookings by Date Range
//    @GetMapping("/date-range")
//    public ResponseEntity<List<BookingDTO>> getBookingsByDateRange(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        List<BookingDTO> bookings = bookingService.getBookingsByDateRange(startDate, endDate);
//        return ResponseEntity.ok(bookings);
//    }
//
//    // Get Recent Bookings
//    @GetMapping("/recent")
//    public ResponseEntity<List<BookingDTO>> getRecentBookings(@RequestParam(defaultValue = "5") int limit) {
//        List<BookingDTO> bookings = bookingService.getRecentBookings(limit);
//        return ResponseEntity.ok(bookings);
//    }
//
//    // Get Total Bookings Count
//    @GetMapping("/count")
//    public ResponseEntity<Long> getTotalBookings() {
//        long count = bookingService.getTotalBookings();
//        return ResponseEntity.ok(count);
//    }
//
//    // Get Booking Count by Date
//    @GetMapping("/count/{date}")
//    public ResponseEntity<Long> getBookingCountByDate(
//            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        long count = bookingService.getBookingCountByDate(date);
//        return ResponseEntity.ok(count);
//    }
//}