package com.rudrabannataxiservices.rudrabannataxiservices.service;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.GalleryImageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GalleryImageService {

    // Create
    GalleryImageDTO createGalleryImage(GalleryImageDTO galleryImageDTO);
    GalleryImageDTO createGalleryImageWithFile(MultipartFile file, String caption);

    // Update
    GalleryImageDTO updateGalleryImage(Integer id, GalleryImageDTO galleryImageDTO);
    GalleryImageDTO updateGalleryImageWithFile(Integer id, MultipartFile file,
                                               String caption, Boolean isActive);

    // Delete
    void deleteGalleryImage(Integer id);
    void bulkDeleteGalleryImages(List<Integer> ids);

    // Read
    GalleryImageDTO getGalleryImageById(Integer id);
    Page<GalleryImageDTO> getAllGalleryImages(Pageable pageable);
    List<GalleryImageDTO> getActiveGalleryImages();
    List<GalleryImageDTO> getInactiveGalleryImages();
    List<GalleryImageDTO> searchGalleryImages(String keyword);
    long getTotalGalleryImages();

    // Status
    GalleryImageDTO toggleGalleryImageStatus(Integer id);
}