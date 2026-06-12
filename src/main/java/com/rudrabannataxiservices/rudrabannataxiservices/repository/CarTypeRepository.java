package com.rudrabannataxiservices.rudrabannataxiservices.repository;

import com.rudrabannataxiservices.rudrabannataxiservices.model.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarTypeRepository extends JpaRepository<CarType, Long> {

    // Find car type by slug
    Optional<CarType> findBySlug(String slug);

    // Find car type by category name
    Optional<CarType> findByCarCategoryName(String carCategoryName);

    // Find all active car types
    List<CarType> findByIsActiveTrue();

    // Check if slug exists
    boolean existsBySlug(String slug);

    // Check if category name exists
    boolean existsByCarCategoryName(String carCategoryName);
}