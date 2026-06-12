package com.rudrabannataxiservices.rudrabannataxiservices.service.impl;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.BookingDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.model.Booking;
import com.rudrabannataxiservices.rudrabannataxiservices.model.Car;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.BookingRepository;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.CarRepository;
import com.rudrabannataxiservices.rudrabannataxiservices.service.BookingService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        // Validate required fields
        if (bookingDTO.getCustomerName() == null || bookingDTO.getCustomerName().trim().isEmpty()) {
            throw new RuntimeException("Customer name is required");
        }
        if (bookingDTO.getCustomerPhone() == null || bookingDTO.getCustomerPhone().trim().isEmpty()) {
            throw new RuntimeException("Customer phone is required");
        }
        if (bookingDTO.getPickupDate() == null) {
            throw new RuntimeException("Pickup date is required");
        }
        if (bookingDTO.getPickupTime() == null) {
            throw new RuntimeException("Pickup time is required");
        }
        if (bookingDTO.getCarId() == null) {
            throw new RuntimeException("Car selection is required");
        }

        // Check if car exists
        Car car = carRepository.findById(bookingDTO.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + bookingDTO.getCarId()));

        // Generate unique booking number
        String bookingNumber = generateBookingNumber();

        // Convert DTO to Entity
        Booking booking = new Booking();
        booking.setBookingNumber(bookingNumber);
        booking.setCar(car);
        booking.setCustomerName(bookingDTO.getCustomerName());
        booking.setCustomerPhone(bookingDTO.getCustomerPhone());
        booking.setCustomerEmail(bookingDTO.getCustomerEmail());
        booking.setPickupLocation(bookingDTO.getPickupLocation());
        booking.setDropLocation(bookingDTO.getDropLocation());
        booking.setPickupDate(bookingDTO.getPickupDate());
        booking.setPickupTime(bookingDTO.getPickupTime());
        booking.setPassengerCount(bookingDTO.getPassengerCount() != null ?
                bookingDTO.getPassengerCount() : 1);
        booking.setSpecialNote(bookingDTO.getSpecialNote());

        // FIX: Make sure this saves properly
        Booking savedBooking = bookingRepository.save(booking);

        // Log to confirm
        System.out.println("Booking saved successfully: " + savedBooking.getBookingNumber());

        return convertToDTO(savedBooking);
    }
    @Override
    public Page<BookingDTO> getAllBookings(Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findAll(pageable);
        return bookings.map(this::convertToDTO);
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return convertToDTO(booking);
    }

    @Override
    public BookingDTO getBookingByNumber(String bookingNumber) {
        Booking booking = bookingRepository.findByBookingNumber(bookingNumber)
                .orElseThrow(() -> new RuntimeException("Booking not found with number: " + bookingNumber));
        return convertToDTO(booking);
    }

    @Override
    public Page<BookingDTO> getFilteredBookings(String customerName, String customerPhone,
                                                String customerEmail, Long carId,
                                                LocalDate pickupDate, LocalDate startDate,
                                                LocalDate endDate, Pageable pageable) {

        // Dynamic filtering using Specification
        Specification<Booking> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by customer name (partial match, case insensitive)
            if (customerName != null && !customerName.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("customerName")),
                        "%" + customerName.toLowerCase() + "%"
                ));
            }

            // Filter by customer phone (partial match)
            if (customerPhone != null && !customerPhone.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        root.get("customerPhone"),
                        "%" + customerPhone + "%"
                ));
            }

            // Filter by customer email (partial match)
            if (customerEmail != null && !customerEmail.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("customerEmail")),
                        "%" + customerEmail.toLowerCase() + "%"
                ));
            }

            // Filter by car
            if (carId != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("car").get("carId"), carId
                ));
            }

            // Filter by exact pickup date
            if (pickupDate != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("pickupDate"), pickupDate
                ));
            }

            // Filter by date range
            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("pickupDate"), startDate, endDate
                ));
            } else if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("pickupDate"), startDate
                ));
            } else if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("pickupDate"), endDate
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Booking> bookings = bookingRepository.findAll(spec, pageable);
        return bookings.map(this::convertToDTO);
    }

    @Override
    public Page<BookingDTO> searchBookingsByCustomer(String keyword, Pageable pageable) {
        Page<Booking> bookings = bookingRepository.searchByCustomerName(keyword, pageable);
        return bookings.map(this::convertToDTO);
    }

    @Override
    public List<BookingDTO> getBookingsByCar(Long carId) {
        List<Booking> bookings = bookingRepository.findByCar_CarId(carId);
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByDate(LocalDate date) {
        List<Booking> bookings = bookingRepository.findByPickupDate(date);
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Booking> bookings = bookingRepository.findByPickupDateBetween(startDate, endDate);
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        // Update fields if provided
        if (bookingDTO.getCustomerName() != null) {
            booking.setCustomerName(bookingDTO.getCustomerName());
        }
        if (bookingDTO.getCustomerPhone() != null) {
            booking.setCustomerPhone(bookingDTO.getCustomerPhone());
        }
        if (bookingDTO.getCustomerEmail() != null) {
            booking.setCustomerEmail(bookingDTO.getCustomerEmail());
        }
        if (bookingDTO.getPickupLocation() != null) {
            booking.setPickupLocation(bookingDTO.getPickupLocation());
        }
        if (bookingDTO.getDropLocation() != null) {
            booking.setDropLocation(bookingDTO.getDropLocation());
        }
        if (bookingDTO.getPickupDate() != null) {
            booking.setPickupDate(bookingDTO.getPickupDate());
        }
        if (bookingDTO.getPickupTime() != null) {
            booking.setPickupTime(bookingDTO.getPickupTime());
        }
        if (bookingDTO.getPassengerCount() != null) {
            booking.setPassengerCount(bookingDTO.getPassengerCount());
        }
        if (bookingDTO.getSpecialNote() != null) {
            booking.setSpecialNote(bookingDTO.getSpecialNote());
        }
        if (bookingDTO.getCarId() != null) {
            Car car = carRepository.findById(bookingDTO.getCarId())
                    .orElseThrow(() -> new RuntimeException("Car not found"));
            booking.setCar(car);
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return convertToDTO(updatedBooking);
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    @Override
    public long getTotalBookings() {
        return bookingRepository.count();
    }

    @Override
    public long getBookingCountByDate(LocalDate date) {
        return bookingRepository.countBookingsByDate(date);
    }

    @Override
    public List<BookingDTO> getRecentBookings(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Booking> bookings = bookingRepository.findRecentBookings(pageable);
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Generate unique booking number
    private String generateBookingNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%04d", new Random().nextInt(10000));
        return "BK" + datePart + randomPart;
    }

    // Helper method: Entity to DTO conversion
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setBookingNumber(booking.getBookingNumber());
        dto.setCarId(booking.getCar().getCarId());
        dto.setCarName(booking.getCar().getName());
        dto.setCustomerName(booking.getCustomerName());
        dto.setCustomerPhone(booking.getCustomerPhone());
        dto.setCustomerEmail(booking.getCustomerEmail());
        dto.setPickupLocation(booking.getPickupLocation());
        dto.setDropLocation(booking.getDropLocation());
        dto.setPickupDate(booking.getPickupDate());
        dto.setPickupTime(booking.getPickupTime());
        dto.setPassengerCount(booking.getPassengerCount());
        dto.setSpecialNote(booking.getSpecialNote());
        dto.setCreatedAt(booking.getCreatedAt());
        return dto;
    }
}