package com.rudrabannataxiservices.rudrabannataxiservices.Appconstant;

public class CarApiConstants {

    private CarApiConstants() {
    }

    // ==================== BASE URL ====================
    public static final String BASE_URL = "/api/cars";

    // ==================== COMMON PATHS ====================
    public static final String BY_ID = "/{id}";
    public static final String BY_SLUG = "/slug/{slug}";
    public static final String FILTER = "/filter";
    public static final String FEATURED = "/featured";
    public static final String BY_TYPE = "/type/{carTypeId}";
    public static final String WITH_IMAGE = "/with-image";
    public static final String UPDATE_WITH_IMAGE = "/{id}/with-image";
    public static final String SEARCH = "/search";
    public static final String PRICE_RANGE = "/price-range";
    public static final String COUNT = "/count";

    public static final String TOGGLE_STATUS = "/{id}/toggle-status";
    public static final String SET_STATUS = "/{id}/status";
    public static final String ACTIVE_CARS = "/active";


    // ==================== SUCCESS MESSAGES ====================
    public static final String CAR_DELETED = "Car deleted successfully";
}