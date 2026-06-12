package com.rudrabannataxiservices.rudrabannataxiservices.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long carId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_type_id", nullable = false)
    private CarType carType;

    @Column(length = 120, nullable = false)
    private String name;

    @Column(length = 150, unique = true)
    private String slug;

    @Column(name = "image_path", length = 500, nullable = false)
    private String imagePath;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity = 4;

    @Column(name = "luggage_capacity")
    private Integer luggageCapacity = 2;

    @Column(name = "per_km_rate", precision = 10, scale = 2, nullable = false)
    private BigDecimal perKmRate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 20, nullable = false)
    private String status = "active";

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", carType=" + carType +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", seatingCapacity=" + seatingCapacity +
                ", luggageCapacity=" + luggageCapacity +
                ", perKmRate=" + perKmRate +
                ", description='" + description + '\'' +
                ", isFeatured=" + isFeatured +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status='" + status + '\'' +
                ", bookings=" + bookings +
                '}';
    }

    // Constructors
    public Car() {}

    public Car(Long carId, CarType carType, String name, String slug, String imagePath,
               Integer seatingCapacity, Integer luggageCapacity, BigDecimal perKmRate,
               String description, Boolean isFeatured, LocalDateTime createdAt,
               LocalDateTime updatedAt,  String status, List<Booking> bookings) {
        this.carId = carId;
        this.carType = carType;
        this.name = name;
        this.slug = slug;
        this.imagePath = imagePath;
        this.seatingCapacity = seatingCapacity;
        this.luggageCapacity = luggageCapacity;
        this.perKmRate = perKmRate;
        this.description = description;
        this.isFeatured = isFeatured;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.bookings = bookings;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getCarId() {
        return carId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public Integer getLuggageCapacity() {
        return luggageCapacity;
    }

    public void setLuggageCapacity(Integer luggageCapacity) {
        this.luggageCapacity = luggageCapacity;
    }

    public BigDecimal getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(BigDecimal perKmRate) {
        this.perKmRate = perKmRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}