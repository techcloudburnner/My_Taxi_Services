package com.rudrabannataxiservices.rudrabannataxiservices.controller;

import com.rudrabannataxiservices.rudrabannataxiservices.Appconstant.GalleryImageApiConstants;
import com.rudrabannataxiservices.rudrabannataxiservices.dto.GalleryImageDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.service.GalleryImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(GalleryImageApiConstants.BASE_URL)
@CrossOrigin(origins = "*")
public class GalleryImageController {

    @Autowired
    private GalleryImageService galleryImageService;

    // =========================================================
    // UPLOAD SINGLE IMAGE
    // POST: /api/gallery/upload
    // =========================================================
    @PostMapping(
            value = GalleryImageApiConstants.UPLOAD,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<GalleryImageDTO> uploadSingleImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption) {

        GalleryImageDTO createdImage = galleryImageService.createGalleryImageWithFile(file, caption);
        return new ResponseEntity<>(createdImage, HttpStatus.CREATED);
    }

    // =========================================================
    // UPLOAD MULTIPLE IMAGES
    // POST: /api/gallery/upload/multiple
    // =========================================================
    @PostMapping(
            value = GalleryImageApiConstants.UPLOAD_MULTIPLE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<List<GalleryImageDTO>> uploadMultipleImages(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "caption", required = false) String caption) {

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }

        List<GalleryImageDTO> uploadedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                GalleryImageDTO createdImage = galleryImageService.createGalleryImageWithFile(file, caption);
                uploadedImages.add(createdImage);
            }
        }

        return new ResponseEntity<>(uploadedImages, HttpStatus.CREATED);
    }

    // =========================================================
    // CREATE WITH JSON
    // POST: /api/gallery
    // =========================================================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GalleryImageDTO> createGalleryImage(
            @RequestBody GalleryImageDTO galleryImageDTO) {

        GalleryImageDTO createdImage = galleryImageService.createGalleryImage(galleryImageDTO);
        return new ResponseEntity<>(createdImage, HttpStatus.CREATED);
    }

    // =========================================================
    // UPDATE WITH NEW FILE
    // PUT: /api/gallery/{id}/with-image
    // =========================================================
    @PutMapping(
            value = GalleryImageApiConstants.UPDATE_WITH_IMAGE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<GalleryImageDTO> updateGalleryImageWithFile(
            @PathVariable Integer id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "isActive", required = false) Boolean isActive) {

        GalleryImageDTO updatedImage = galleryImageService.updateGalleryImageWithFile(
                id, file, caption, isActive);
        return ResponseEntity.ok(updatedImage);
    }

    // =========================================================
    // BULK DELETE
    // DELETE: /api/gallery/bulk-delete
    // =========================================================
    @DeleteMapping(GalleryImageApiConstants.BULK_DELETE)
    public ResponseEntity<String> bulkDeleteGalleryImages(
            @RequestBody List<Integer> ids) {

        galleryImageService.bulkDeleteGalleryImages(ids);
        return ResponseEntity.ok(GalleryImageApiConstants.GALLERY_IMAGES_DELETED);
    }

    // =========================================================
    // GET ACTIVE IMAGES
    // GET: /api/gallery/active
    // =========================================================
    @GetMapping(GalleryImageApiConstants.ACTIVE)
    public ResponseEntity<List<GalleryImageDTO>> getActiveGalleryImages() {
        List<GalleryImageDTO> images = galleryImageService.getActiveGalleryImages();
        return ResponseEntity.ok(images);
    }

    // =========================================================
    // GET INACTIVE IMAGES
    // GET: /api/gallery/inactive
    // =========================================================
    @GetMapping(GalleryImageApiConstants.INACTIVE)
    public ResponseEntity<List<GalleryImageDTO>> getInactiveGalleryImages() {
        List<GalleryImageDTO> images = galleryImageService.getInactiveGalleryImages();
        return ResponseEntity.ok(images);
    }

    // =========================================================
    // SEARCH IMAGES
    // GET: /api/gallery/search?keyword=car
    // =========================================================
    @GetMapping(GalleryImageApiConstants.SEARCH)
    public ResponseEntity<List<GalleryImageDTO>> searchGalleryImages(
            @RequestParam String keyword) {

        List<GalleryImageDTO> images = galleryImageService.searchGalleryImages(keyword);
        return ResponseEntity.ok(images);
    }

    // =========================================================
    // GET TOTAL COUNT
    // GET: /api/gallery/count
    // =========================================================
    @GetMapping(GalleryImageApiConstants.COUNT)
    public ResponseEntity<Long> getTotalGalleryImages() {
        long count = galleryImageService.getTotalGalleryImages();
        return ResponseEntity.ok(count);
    }

    // =========================================================
    // GET ALL IMAGES (Paginated)
    // GET: /api/gallery?page=0&size=12&sort=createdAt,desc
    // =========================================================
    @GetMapping
    public ResponseEntity<Page<GalleryImageDTO>> getAllGalleryImages(
            @PageableDefault(
                    size = 12,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        Page<GalleryImageDTO> images = galleryImageService.getAllGalleryImages(pageable);
        return ResponseEntity.ok(images);
    }

    // =========================================================
    // TOGGLE STATUS
    // PATCH: /api/gallery/{id}/toggle-status
    // =========================================================
    @PatchMapping(GalleryImageApiConstants.TOGGLE_STATUS)
    public ResponseEntity<GalleryImageDTO> toggleGalleryImageStatus(
            @PathVariable Integer id) {

        GalleryImageDTO image = galleryImageService.toggleGalleryImageStatus(id);
        return ResponseEntity.ok(image);
    }

    // =========================================================
    // GET BY ID
    // GET: /api/gallery/{id}
    // =========================================================
    @GetMapping(GalleryImageApiConstants.BY_ID)
    public ResponseEntity<GalleryImageDTO> getGalleryImageById(
            @PathVariable Integer id) {

        GalleryImageDTO image = galleryImageService.getGalleryImageById(id);
        return ResponseEntity.ok(image);
    }

    // =========================================================
    // UPDATE IMAGE JSON (Without File)
    // PUT: /api/gallery/{id}
    // =========================================================
    @PutMapping(
            value = GalleryImageApiConstants.BY_ID,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GalleryImageDTO> updateGalleryImage(
            @PathVariable Integer id,
            @RequestBody GalleryImageDTO galleryImageDTO) {

        GalleryImageDTO updatedImage = galleryImageService.updateGalleryImage(id, galleryImageDTO);
        return ResponseEntity.ok(updatedImage);
    }

    // =========================================================
    // DELETE IMAGE
    // DELETE: /api/gallery/{id}
    // =========================================================
    @DeleteMapping(GalleryImageApiConstants.BY_ID)
    public ResponseEntity<String> deleteGalleryImage(
            @PathVariable Integer id) {

        galleryImageService.deleteGalleryImage(id);
        return ResponseEntity.ok(GalleryImageApiConstants.GALLERY_IMAGE_DELETED);
    }
}