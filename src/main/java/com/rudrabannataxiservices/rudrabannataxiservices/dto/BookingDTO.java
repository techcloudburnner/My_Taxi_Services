package com.rudrabannataxiservices.rudrabannataxiservices.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class BookingDTO {

    private Long bookingId;
    private String bookingNumber;
    private Long carId;
    private String carName;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String pickupLocation;
    private String dropLocation;
    private LocalDate pickupDate;
    private LocalTime pickupTime;
    private Integer passengerCount;
    private String specialNote;
    private LocalDateTime createdAt;

    // Constructors
    public BookingDTO() {}

    public BookingDTO(Long bookingId, String bookingNumber, Long carId, String carName,
                      String customerName, String customerPhone, String customerEmail,
                      String pickupLocation, String dropLocation, LocalDate pickupDate,
                      LocalTime pickupTime, Integer passengerCount, String specialNote,
                      LocalDateTime createdAt) {
        this.bookingId = bookingId;
        this.bookingNumber = bookingNumber;
        this.carId = carId;
        this.carName = carName;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.passengerCount = passengerCount;
        this.specialNote = specialNote;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}