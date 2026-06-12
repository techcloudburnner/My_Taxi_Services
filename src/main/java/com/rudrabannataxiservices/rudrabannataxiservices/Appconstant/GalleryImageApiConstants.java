package com.rudrabannataxiservices.rudrabannataxiservices.Appconstant;

public final class GalleryImageApiConstants {

    private GalleryImageApiConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    // ==================== BASE URL ====================
    public static final String BASE_URL = "/api/gallery";

    // ==================== ENDPOINTS ====================
    public static final String UPLOAD = "/upload";
    public static final String UPLOAD_MULTIPLE = "/upload/multiple";
    public static final String UPDATE_WITH_IMAGE = "/{id}/with-image";
    public static final String BULK_DELETE = "/bulk-delete";
    public static final String ACTIVE = "/active";
    public static final String INACTIVE = "/inactive";
    public static final String SEARCH = "/search";
    public static final String COUNT = "/count";
    public static final String TOGGLE_STATUS = "/{id}/toggle-status";
    public static final String BY_ID = "/{id}";

    // ==================== SUCCESS MESSAGES ====================
    public static final String GALLERY_IMAGE_CREATED = "Gallery image created successfully";
    public static final String GALLERY_IMAGES_UPLOADED = "Gallery images uploaded successfully";
    public static final String GALLERY_IMAGE_UPDATED = "Gallery image updated successfully";
    public static final String GALLERY_IMAGE_DELETED = "Gallery image deleted successfully";
    public static final String GALLERY_IMAGES_DELETED = "Gallery images deleted successfully";

    // ==================== ERROR MESSAGES ====================
    public static final String ERROR_NOT_FOUND = "Gallery image not found with id: ";
}