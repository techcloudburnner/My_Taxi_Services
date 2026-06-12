package com.rudrabannataxiservices.rudrabannataxiservices.Appconstant;

public class ContactApiConstants {

    private ContactApiConstants() {
    }

    // ==================== BASE URL ====================
    public static final String BASE_URL = "/api/contacts";

    // ==================== COMMON PATHS ====================
    public static final String BY_ID = "/{id}";
    public static final String FILTER = "/filter";
    public static final String SEARCH = "/search";
    public static final String RECENT = "/recent";
    public static final String COUNT = "/count";

    // ==================== SUCCESS MESSAGES ====================
    public static final String CONTACT_DELETED =
            "Contact deleted successfully";
}