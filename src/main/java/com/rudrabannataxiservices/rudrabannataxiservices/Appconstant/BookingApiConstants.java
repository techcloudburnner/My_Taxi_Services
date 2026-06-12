package com.rudrabannataxiservices.rudrabannataxiservices.Appconstant;

public class BookingApiConstants {

    private BookingApiConstants() {
    }

    // ==================== BASE URL ====================
    public static final String BASE_URL = "/api/bookings";

    // ==================== COMMON PATHS ====================
    public static final String BY_ID = "/{id}";
    public static final String BY_BOOKING_NUMBER = "/number/{bookingNumber}";
    public static final String FILTER = "/filter";
    public static final String SEARCH = "/search";
    public static final String BY_CAR = "/car/{carId}";
    public static final String BY_DATE = "/date/{date}";
    public static final String DATE_RANGE = "/date-range";
    public static final String RECENT = "/recent";
    public static final String COUNT = "/count";
    public static final String COUNT_BY_DATE = "/count/{date}";

    // ==================== SUCCESS MESSAGES ====================
    public static final String BOOKING_DELETED = "Booking deleted successfully";
}