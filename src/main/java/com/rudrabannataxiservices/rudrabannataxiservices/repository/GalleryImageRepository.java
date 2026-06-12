package com.rudrabannataxiservices.rudrabannataxiservices.repository;

import com.rudrabannataxiservices.rudrabannataxiservices.model.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GalleryImageRepository extends JpaRepository<GalleryImage, Integer> {

    // Find all active gallery images
    List<GalleryImage> findByIsActiveTrue();

    // Find all inactive gallery images
    List<GalleryImage> findByIsActiveFalse();

    // Find gallery images by caption containing keyword
    List<GalleryImage> findByCaptionContainingIgnoreCase(String keyword);
}