package com.rudrabannataxiservices.rudrabannataxiservices.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarDTO {

    private Long carId;
    private Long carTypeId;
    private String carTypeName;
    private String name;
    private String slug;
    private String imagePath;
    private Integer seatingCapacity;
    private Integer luggageCapacity;
    private BigDecimal perKmRate;
    private String description;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    // Add these getter and setter

    // Constructors
    public CarDTO() {}

    public CarDTO(Long carId, Long carTypeId, String carTypeName, String name, String slug,
                  String imagePath, Integer seatingCapacity, Integer luggageCapacity,
                  BigDecimal perKmRate, String description, Boolean isFeatured,
                  LocalDateTime createdAt, String status, LocalDateTime updatedAt) {
        this.carId = carId;
        this.carTypeId = carTypeId;
        this.carTypeName = carTypeName;
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
        this.status=status;

    }

    // Getters and Setters
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Long getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Long carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}