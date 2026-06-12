    package com.rudrabannataxiservices.rudrabannataxiservices.service;

    import org.springframework.stereotype.Service;
    import org.springframework.util.StringUtils;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.nio.file.*;
    import java.util.*;

    @Service
    public class FileStorageService {

    //    private static final Path ROOT = Paths.get(System.getProperty("user.dir"), "uploads");

        private static final Path ROOT =
                Paths.get("/opt/rudra-taxi/uploads");
    //            Paths.get(
    //                    System.getProperty("catalina.base"),
    //                    "webapps",
    //                    "rudrabannataxiservices",
    //                    "uploads"
    //            );
        private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
                ".jpg", ".jpeg", ".png", ".webp", ".gif", ".bmp"
        ));

        private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
                "image/jpeg", "image/png", "image/webp", "image/gif", "image/bmp"
        ));

        private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB - Updated to 20MB

        // Folder names
        public static final String CARS_FOLDER = "cars";
        public static final String GALLERY_FOLDER = "gallery";

        public FileStorageService() {
            try {
                Files.createDirectories(ROOT.resolve(CARS_FOLDER));
                Files.createDirectories(ROOT.resolve(GALLERY_FOLDER));
                System.out.println("✅ Upload directories created at: " + ROOT.toAbsolutePath());
                System.out.println("   - Cars: " + ROOT.resolve(CARS_FOLDER).toAbsolutePath());
                System.out.println("   - Gallery: " + ROOT.resolve(GALLERY_FOLDER).toAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize upload directories", e);
            }
        }

        public String storeCarImage(MultipartFile file) {
            return storeFile(file, CARS_FOLDER);
        }

        public String storeGalleryImage(MultipartFile file) {
            return storeFile(file, GALLERY_FOLDER);
        }

        public String storeFile(MultipartFile file, String folderName) {
            try {
                // Validate empty
                if (file == null || file.isEmpty()) {
                    throw new RuntimeException("File is empty");
                }

                // Validate size (20MB)
                if (file.getSize() > MAX_FILE_SIZE) {
                    throw new RuntimeException("File size too large. Max: " + (MAX_FILE_SIZE / (1024 * 1024)) + "MB");
                }

                // Validate filename
                String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
                if (originalFilename == null || !originalFilename.contains(".")) {
                    throw new RuntimeException("Invalid file name");
                }

                // Validate extension
                String ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
                if (!ALLOWED_EXTENSIONS.contains(ext)) {
                    throw new RuntimeException("Invalid file type: " + ext + ". Allowed: " + ALLOWED_EXTENSIONS);
                }

                // Validate MIME type
                String mimeType = file.getContentType();
                if (mimeType != null && !ALLOWED_MIME_TYPES.contains(mimeType)) {
                    throw new RuntimeException("Invalid MIME type: " + mimeType);
                }

                // Create folder
                Path folderPath = ROOT.resolve(folderName).normalize();
                Files.createDirectories(folderPath);

                // Generate unique name
                String fileName = UUID.randomUUID().toString() + ext;

                // Save file
                Path target = folderPath.resolve(fileName).normalize();
                Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

                // Return path with /uploads/ prefix for proper URL access
                String relativePath = "/uploads/" + folderName + "/" + fileName;
                System.out.println("✅ Image saved: " + relativePath + " -> " + target.toAbsolutePath());

                return relativePath;

            } catch (IOException e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }

        public void deleteFile(String relativePath) {
            try {
                if (relativePath == null || relativePath.isBlank()) {
                    System.out.println("⚠️ No file path provided for deletion");
                    return;
                }

                // Remove /uploads/ prefix if present to get relative path
                String cleanPath = relativePath;
                if (cleanPath.startsWith("/uploads/")) {
                    cleanPath = cleanPath.substring(9); // Remove "/uploads/"
                }

                Path filePath = ROOT.resolve(cleanPath).normalize();

                // Security check - ensure file is within uploads directory
                if (!filePath.startsWith(ROOT)) {
                    System.err.println("❌ Security: Attempted to delete file outside uploads directory: " + relativePath);
                    return;
                }

                boolean deleted = Files.deleteIfExists(filePath);
                if (deleted) {
                    System.out.println("🗑️ File deleted: " + relativePath + " -> " + filePath.toAbsolutePath());
                } else {
                    System.out.println("⚠️ File not found for deletion: " + relativePath);
                }
            } catch (IOException e) {
                System.err.println("❌ Failed to delete file: " + relativePath + " - " + e.getMessage());
            }
        }

        public Path getFullPath(String relativePath) {
            String cleanPath = relativePath;
            if (cleanPath.startsWith("/uploads/")) {
                cleanPath = cleanPath.substring(9);
            }
            return ROOT.resolve(cleanPath).normalize();
        }

        // Helper method to get accessible URL from stored path
        public String getAccessibleUrl(String storedPath) {
            if (storedPath == null || storedPath.isBlank()) {
                return null;
            }

            // If path already starts with /uploads/, return as is
            if (storedPath.startsWith("/uploads/")) {
                return storedPath;
            }

            // If path is like "gallery/filename.png", convert to "/uploads/gallery/filename.png"
            if (storedPath.contains("/")) {
                return "/uploads/" + storedPath;
            }

            // Default case
            return "/uploads/" + storedPath;
        }
    }
    //package com.rudrabannataxiservices.rudrabannataxiservices.service;
    //
    //import org.springframework.stereotype.Service;
    //import org.springframework.util.StringUtils;
    //import org.springframework.web.multipart.MultipartFile;
    //
    //import java.io.IOException;
    //import java.nio.file.*;
    //import java.util.*;
    //
    //@Service
    //public class FileStorageService {
    //
    //    private static final Path ROOT = Paths.get(System.getProperty("user.dir"), "uploads");
    //
    //    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
    //            ".jpg", ".jpeg", ".png", ".webp", ".gif", ".bmp"
    //    ));
    //
    //    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
    //            "image/jpeg", "image/png", "image/webp", "image/gif", "image/bmp"
    //    ));
    //
    //    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    //
    //    // Folder names
    //    public static final String CARS_FOLDER = "cars";
    //    public static final String GALLERY_FOLDER = "gallery";
    //
    //    public FileStorageService() {
    //        try {
    //            Files.createDirectories(ROOT.resolve(CARS_FOLDER));
    //            Files.createDirectories(ROOT.resolve(GALLERY_FOLDER));
    //            System.out.println("Upload directories created at: " + ROOT.toAbsolutePath());
    //        } catch (IOException e) {
    //            throw new RuntimeException("Could not initialize upload directories", e);
    //        }
    //    }
    //
    //    public String storeCarImage(MultipartFile file) {
    //        return storeFile(file, CARS_FOLDER);
    //    }
    //
    //    public String storeGalleryImage(MultipartFile file) {
    //        return storeFile(file, GALLERY_FOLDER);
    //    }
    //
    //    public String storeFile(MultipartFile file, String folderName) {
    //        try {
    //            // Validate empty
    //            if (file == null || file.isEmpty()) {
    //                throw new RuntimeException("File is empty");
    //            }
    //
    //            // Validate size
    //            if (file.getSize() > MAX_FILE_SIZE) {
    //                throw new RuntimeException("File size too large. Max: " + (MAX_FILE_SIZE / (1024 * 1024)) + "MB");
    //            }
    //
    //            // Validate filename
    //            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
    //            if (originalFilename == null || !originalFilename.contains(".")) {
    //                throw new RuntimeException("Invalid file name");
    //            }
    //
    //            // Validate extension
    //            String ext = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
    //            if (!ALLOWED_EXTENSIONS.contains(ext)) {
    //                throw new RuntimeException("Invalid file type: " + ext + ". Allowed: " + ALLOWED_EXTENSIONS);
    //            }
    //
    //            // Create folder
    //            Path folderPath = ROOT.resolve(folderName).normalize();
    //            Files.createDirectories(folderPath);
    //
    //            // Generate unique name
    //            String fileName = UUID.randomUUID().toString() + ext;
    //
    //            // Save file
    //            Path target = folderPath.resolve(fileName).normalize();
    //            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
    //
    //            String relativePath = folderName + "/" + fileName;
    //            System.out.println("Image saved: " + relativePath);
    //
    //            return relativePath;
    //
    //        } catch (IOException e) {
    //            throw new RuntimeException("Failed to store file", e);
    //        }
    //    }
    //
    //    public void deleteFile(String relativePath) {
    //        try {
    //            if (relativePath == null || relativePath.isBlank()) return;
    //            Path filePath = ROOT.resolve(relativePath).normalize();
    //            Files.deleteIfExists(filePath);
    //            System.out.println("File deleted: " + relativePath);
    //        } catch (IOException e) {
    //            System.err.println("Failed to delete file: " + relativePath);
    //        }
    //    }
    //
    //    public Path getFullPath(String relativePath) {
    //        return ROOT.resolve(relativePath).normalize();
    //    }
    //}