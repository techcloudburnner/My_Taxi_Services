package com.rudrabannataxiservices.rudrabannataxiservices.dto;

import java.time.LocalDateTime;

public class GalleryImageDTO {

    private Integer id;
    private String imagePath;
    private String caption;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;  // 🆕 Added

    // Constructors
    public GalleryImageDTO() {}

    public GalleryImageDTO(Integer id, String imagePath, String caption,
                           Boolean isActive, LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
        this.id = id;
        this.imagePath = imagePath;
        this.caption = caption;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // 🆕 Added
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}