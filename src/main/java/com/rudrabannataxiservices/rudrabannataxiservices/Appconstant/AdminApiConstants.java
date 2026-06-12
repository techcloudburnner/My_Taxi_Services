package com.rudrabannataxiservices.rudrabannataxiservices.Appconstant;

public final class AdminApiConstants {

    private AdminApiConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    // Base URL
    public static final String BASE_URL = "/api/admins";

    // Endpoints
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/{id}/logout";
    public static final String VALIDATE_SESSION = "/validate-session";
    public static final String CHECK_USERNAME = "/check-username";
    public static final String COUNT = "/count";
    public static final String BY_ID = "/{id}";

    // Success Messages
    public static final String ADMIN_CREATED = "Admin created successfully";
    public static final String ADMIN_UPDATED = "Admin updated successfully";
    public static final String ADMIN_DELETED = "Admin deleted successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logged out successfully";

    // Error Messages
    public static final String ERROR_NOT_FOUND = "Admin not found with id: ";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ERROR_USERNAME_EXISTS = "Username already exists";
}