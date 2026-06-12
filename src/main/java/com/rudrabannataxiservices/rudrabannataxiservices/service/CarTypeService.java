package com.rudrabannataxiservices.rudrabannataxiservices.service;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.CarTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CarTypeService {

    // Create car type
    CarTypeDTO createCarType(CarTypeDTO carTypeDTO);

    // Update car type
    CarTypeDTO updateCarType(Long id, CarTypeDTO carTypeDTO);

    // Delete car type
    void deleteCarType(Long id);

    // Get car type by ID
    CarTypeDTO getCarTypeById(Long id);

    // Get car type by slug
    CarTypeDTO getCarTypeBySlug(String slug);

    // Get all car types with pagination
    Page<CarTypeDTO> getAllCarTypes(Pageable pageable);

    // Get all active car types
    List<CarTypeDTO> getActiveCarTypes();

    // Toggle car type active status
    CarTypeDTO toggleCarTypeStatus(Long id);

    // Check if slug exists
    boolean isSlugExists(String slug);

    // Get total car types count
    long getTotalCarTypes();

    // Get car types with car count
    List<CarTypeDTO> getCarTypesWithCarCount();
}