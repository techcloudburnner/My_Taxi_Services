package com.rudrabannataxiservices.rudrabannataxiservices.repository;

import com.rudrabannataxiservices.rudrabannataxiservices.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;  // Add this import
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long>,
        JpaSpecificationExecutor<Car> {  // Add this

    // Find car by slug
    Optional<Car> findBySlug(String slug);

    // Find all featured cars
    List<Car> findByIsFeaturedTrue();

    // Find cars by car type
    List<Car> findByCarType_CarTypeId(Long carTypeId);

    // Find cars by car type and active status
    List<Car> findByCarType_CarTypeIdAndIsFeaturedTrue(Long carTypeId);

    // Find cars by seating capacity
    List<Car> findBySeatingCapacityGreaterThanEqual(Integer seatingCapacity);

    // Find cars by price range
    List<Car> findByPerKmRateBetween(BigDecimal minRate, BigDecimal maxRate);

    // Custom JPQL Query - Find cars by car type name
    @Query("SELECT c FROM Car c WHERE c.carType.carCategoryName = :categoryName")
    List<Car> findCarsByCategoryName(@Param("categoryName") String categoryName);

    // Custom JPQL Query - Find featured cars with specific seating capacity
    @Query("SELECT c FROM Car c WHERE c.isFeatured = true AND c.seatingCapacity >= :minSeats")
    List<Car> findFeaturedCarsBySeatingCapacity(@Param("minSeats") Integer minSeats);

    // Custom Native Query - Search cars by name (case insensitive)
    @Query(value = "SELECT * FROM cars WHERE LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    List<Car> searchCarsByName(@Param("keyword") String keyword);

    // Count cars by car type
    long countByCarType_CarTypeId(Long carTypeId);

    // Check if slug exists
    boolean existsBySlug(String slug);
}