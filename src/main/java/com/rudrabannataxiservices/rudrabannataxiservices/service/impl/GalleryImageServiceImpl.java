package com.rudrabannataxiservices.rudrabannataxiservices.service.impl;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.GalleryImageDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.model.GalleryImage;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.GalleryImageRepository;
import com.rudrabannataxiservices.rudrabannataxiservices.service.FileStorageService;
import com.rudrabannataxiservices.rudrabannataxiservices.service.GalleryImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GalleryImageServiceImpl implements GalleryImageService {

    @Autowired
    private GalleryImageRepository galleryImageRepository;

    @Autowired
    private FileStorageService fileStorageService;

    // =========================================================
    // CREATE OPERATIONS
    // =========================================================

    @Override
    public GalleryImageDTO createGalleryImage(GalleryImageDTO galleryImageDTO) {
        if (galleryImageDTO.getImagePath() == null || galleryImageDTO.getImagePath().trim().isEmpty()) {
            throw new RuntimeException("Image path is required");
        }

        GalleryImage galleryImage = new GalleryImage();
        galleryImage.setImagePath(galleryImageDTO.getImagePath());
        galleryImage.setCaption(galleryImageDTO.getCaption());
        galleryImage.setIsActive(galleryImageDTO.getIsActive() != null ?
                galleryImageDTO.getIsActive() : true);

        GalleryImage savedImage = galleryImageRepository.save(galleryImage);
        return convertToDTO(savedImage);
    }

    @Override
    public GalleryImageDTO createGalleryImageWithFile(MultipartFile file, String caption) {
        String imagePath = fileStorageService.storeGalleryImage(file);

        GalleryImage galleryImage = new GalleryImage();
        galleryImage.setImagePath(imagePath);
        galleryImage.setCaption(caption);
        galleryImage.setIsActive(true);

        GalleryImage savedImage = galleryImageRepository.save(galleryImage);
        return convertToDTO(savedImage);
    }

    // =========================================================
    // UPDATE OPERATIONS
    // =========================================================

    @Override
    public GalleryImageDTO updateGalleryImage(Integer id, GalleryImageDTO galleryImageDTO) {
        GalleryImage galleryImage = galleryImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery image not found with id: " + id));

        if (galleryImageDTO.getImagePath() != null) {
            if (galleryImage.getImagePath() != null &&
                    !galleryImage.getImagePath().equals(galleryImageDTO.getImagePath())) {
                fileStorageService.deleteFile(galleryImage.getImagePath());
            }
            galleryImage.setImagePath(galleryImageDTO.getImagePath());
        }

        if (galleryImageDTO.getCaption() != null) {
            galleryImage.setCaption(galleryImageDTO.getCaption());
        }

        if (galleryImageDTO.getIsActive() != null) {
            galleryImage.setIsActive(galleryImageDTO.getIsActive());
        }

        GalleryImage updatedImage = galleryImageRepository.save(galleryImage);
        return convertToDTO(updatedImage);
    }

    @Override
    public GalleryImageDTO updateGalleryImageWithFile(Integer id, MultipartFile file,
                                                      String caption, Boolean isActive) {
        GalleryImage galleryImage = galleryImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery image not found with id: " + id));

        if (file != null && !file.isEmpty()) {
            if (galleryImage.getImagePath() != null) {
                fileStorageService.deleteFile(galleryImage.getImagePath());
            }
            String newImagePath = fileStorageService.storeGalleryImage(file);
            galleryImage.setImagePath(newImagePath);
        }

        if (caption != null) {
            galleryImage.setCaption(caption);
        }

        if (isActive != null) {
            galleryImage.setIsActive(isActive);
        }

        GalleryImage updatedImage = galleryImageRepository.save(galleryImage);
        return convertToDTO(updatedImage);
    }

    // =========================================================
    // DELETE OPERATIONS
    // =========================================================

    @Override
    public void deleteGalleryImage(Integer id) {
        GalleryImage galleryImage = galleryImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery image not found with id: " + id));

        if (galleryImage.getImagePath() != null) {
            fileStorageService.deleteFile(galleryImage.getImagePath());
        }

        galleryImageRepository.deleteById(id);
    }

    @Override
    public void bulkDeleteGalleryImages(List<Integer> ids) {
        List<GalleryImage> galleryImages = galleryImageRepository.findAllById(ids);

        for (GalleryImage image : galleryImages) {
            if (image.getImagePath() != null) {
                fileStorageService.deleteFile(image.getImagePath());
            }
        }

        galleryImageRepository.deleteAllById(ids);
    }

    // =========================================================
    // READ OPERATIONS
    // =========================================================

    @Override
    public GalleryImageDTO getGalleryImageById(Integer id) {
        GalleryImage galleryImage = galleryImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery image not found with id: " + id));
        return convertToDTO(galleryImage);
    }

    @Override
    public Page<GalleryImageDTO> getAllGalleryImages(Pageable pageable) {
        Page<GalleryImage> galleryImages = galleryImageRepository.findAll(pageable);
        return galleryImages.map(this::convertToDTO);
    }

    @Override
    public List<GalleryImageDTO> getActiveGalleryImages() {
        List<GalleryImage> galleryImages = galleryImageRepository.findByIsActiveTrue();
        return galleryImages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GalleryImageDTO> getInactiveGalleryImages() {
        List<GalleryImage> galleryImages = galleryImageRepository.findByIsActiveFalse();
        return galleryImages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GalleryImageDTO> searchGalleryImages(String keyword) {
        List<GalleryImage> galleryImages = galleryImageRepository
                .findByCaptionContainingIgnoreCase(keyword);
        return galleryImages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getTotalGalleryImages() {
        return galleryImageRepository.count();
    }

    // =========================================================
    // STATUS OPERATIONS
    // =========================================================

    @Override
    public GalleryImageDTO toggleGalleryImageStatus(Integer id) {
        GalleryImage galleryImage = galleryImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery image not found with id: " + id));

        galleryImage.setIsActive(!galleryImage.getIsActive());
        GalleryImage updatedImage = galleryImageRepository.save(galleryImage);
        return convertToDTO(updatedImage);
    }

    // =========================================================
    // HELPER METHOD
    // =========================================================

    private GalleryImageDTO convertToDTO(GalleryImage galleryImage) {
        GalleryImageDTO dto = new GalleryImageDTO();
        dto.setId(galleryImage.getId());
        dto.setImagePath(galleryImage.getImagePath());
        dto.setCaption(galleryImage.getCaption());
        dto.setIsActive(galleryImage.getIsActive());
        dto.setCreatedAt(galleryImage.getCreatedAt());
        dto.setUpdatedAt(galleryImage.getUpdatedAt());
        return dto;
    }
}