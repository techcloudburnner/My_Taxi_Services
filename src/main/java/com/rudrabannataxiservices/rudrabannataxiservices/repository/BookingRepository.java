package com.rudrabannataxiservices.rudrabannataxiservices.repository;

import com.rudrabannataxiservices.rudrabannataxiservices.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>,
        JpaSpecificationExecutor<Booking> {

    Optional<Booking> findByBookingNumber(String bookingNumber);
    List<Booking> findByCustomerPhone(String customerPhone);
    List<Booking> findByCustomerEmail(String customerEmail);
    List<Booking> findByPickupDate(LocalDate pickupDate);
    List<Booking> findByCar_CarId(Long carId);
    List<Booking> findByPickupDateBetween(LocalDate startDate, LocalDate endDate);
    List<Booking> findByPickupDateAndCar_CarId(LocalDate pickupDate, Long carId);

    // Pagination support
    Page<Booking> findByCustomerPhone(String customerPhone, Pageable pageable);
    Page<Booking> findByPickupDate(LocalDate pickupDate, Pageable pageable);
    Page<Booking> findByCar_CarId(Long carId, Pageable pageable);

    @Query("SELECT b FROM Booking b ORDER BY b.createdAt DESC")
    List<Booking> findRecentBookings(Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE LOWER(b.customerName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Booking> searchByCustomerName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM bookings WHERE pickup_date = :date", nativeQuery = true)
    long countBookingsByDate(@Param("date") LocalDate date);

    long countByCar_CarId(Long carId);
    boolean existsByBookingNumber(String bookingNumber);
}