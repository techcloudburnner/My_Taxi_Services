package com.rudrabannataxiservices.rudrabannataxiservices.service;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    // Create booking
    BookingDTO createBooking(BookingDTO bookingDTO);

    // Get all bookings with pagination
    Page<BookingDTO> getAllBookings(Pageable pageable);

    // Get booking by ID
    BookingDTO getBookingById(Long id);

    // Get booking by booking number
    BookingDTO getBookingByNumber(String bookingNumber);

    // Get bookings with filtering and pagination
    Page<BookingDTO> getFilteredBookings(String customerName, String customerPhone,
                                         String customerEmail, Long carId,
                                         LocalDate pickupDate, LocalDate startDate,
                                         LocalDate endDate, Pageable pageable);

    // Search bookings by customer name
    Page<BookingDTO> searchBookingsByCustomer(String keyword, Pageable pageable);

    // Get bookings by car
    List<BookingDTO> getBookingsByCar(Long carId);

    // Get bookings by date
    List<BookingDTO> getBookingsByDate(LocalDate date);

    // Get bookings by date range
    List<BookingDTO> getBookingsByDateRange(LocalDate startDate, LocalDate endDate);

    // Update booking
    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);

    // Delete booking
    void deleteBooking(Long id);

    // Count total bookings
    long getTotalBookings();

    // Count bookings by date
    long getBookingCountByDate(LocalDate date);

    // Get recent bookings
    List<BookingDTO> getRecentBookings(int limit);
}