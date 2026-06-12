package com.rudrabannataxiservices.rudrabannataxiservices.Appconstant;

public class CarTypeApiConstants {

    private CarTypeApiConstants() {
    }

    // ==================== BASE URL ====================
    public static final String BASE_URL = "/api/car-types";

    // ==================== COMMON PATHS ====================
    public static final String BY_ID = "/{id}";
    public static final String BY_SLUG = "/slug/{slug}";
    public static final String ACTIVE = "/active";
    public static final String TOGGLE_STATUS = "/{id}/toggle-status";
    public static final String CHECK_SLUG = "/check-slug";
    public static final String COUNT = "/count";
    public static final String WITH_CAR_COUNT = "/with-car-count";

    // ==================== SUCCESS MESSAGES ====================
    public static final String CAR_TYPE_DELETED =
            "Car type deleted successfully";
}