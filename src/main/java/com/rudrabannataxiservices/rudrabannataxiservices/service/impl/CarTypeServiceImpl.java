package com.rudrabannataxiservices.rudrabannataxiservices.service.impl;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.CarTypeDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.model.CarType;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.CarRepository;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.CarTypeRepository;
import com.rudrabannataxiservices.rudrabannataxiservices.service.CarTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarTypeServiceImpl implements CarTypeService {

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private CarRepository carRepository;

    @Override
    public CarTypeDTO createCarType(CarTypeDTO carTypeDTO) {
        // Validate
        if (carTypeDTO.getCarCategoryName() == null || carTypeDTO.getCarCategoryName().trim().isEmpty()) {
            throw new RuntimeException("Car category name is required");
        }

        // Check if category name already exists
        if (carTypeRepository.existsByCarCategoryName(carTypeDTO.getCarCategoryName())) {
            throw new RuntimeException("Car category name already exists");
        }

        // Check if slug already exists
        if (carTypeDTO.getSlug() != null && carTypeRepository.existsBySlug(carTypeDTO.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        // Convert DTO to Entity
        CarType carType = new CarType();
        carType.setCarCategoryName(carTypeDTO.getCarCategoryName());
        carType.setSlug(carTypeDTO.getSlug());
        carType.setIsActive(carTypeDTO.getIsActive() != null ? carTypeDTO.getIsActive() : true);

        // Save to database
        CarType savedCarType = carTypeRepository.save(carType);

        // Convert Entity to DTO and return
        return convertToDTO(savedCarType);
    }

    @Override
    public CarTypeDTO updateCarType(Long id, CarTypeDTO carTypeDTO) {
        CarType carType = carTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car type not found with id: " + id));

        // Update fields if provided
        if (carTypeDTO.getCarCategoryName() != null) {
            // Check if new name already exists for different car type
            if (!carType.getCarCategoryName().equals(carTypeDTO.getCarCategoryName()) &&
                    carTypeRepository.existsByCarCategoryName(carTypeDTO.getCarCategoryName())) {
                throw new RuntimeException("Car category name already exists");
            }
            carType.setCarCategoryName(carTypeDTO.getCarCategoryName());
        }

        if (carTypeDTO.getSlug() != null) {
            // Check if new slug already exists for different car type
            if (!carType.getSlug().equals(carTypeDTO.getSlug()) &&
                    carTypeRepository.existsBySlug(carTypeDTO.getSlug())) {
                throw new RuntimeException("Slug already exists");
            }
            carType.setSlug(carTypeDTO.getSlug());
        }

        CarType updatedCarType = carTypeRepository.save(carType);
        return convertToDTO(updatedCarType);
    }

    @Override
    public void deleteCarType(Long id) {
        if (!carTypeRepository.existsById(id)) {
            throw new RuntimeException("Car type not found with id: " + id);
        }

        // Check if any cars are associated with this car type
        long carCount = carRepository.countByCarType_CarTypeId(id);
        if (carCount > 0) {
            throw new RuntimeException("Cannot delete car type. " + carCount + " cars are associated with this type");
        }

        carTypeRepository.deleteById(id);
    }

    @Override
    public CarTypeDTO getCarTypeById(Long id) {
        CarType carType = carTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car type not found with id: " + id));
        return convertToDTO(carType);
    }

    @Override
    public CarTypeDTO getCarTypeBySlug(String slug) {
        CarType carType = carTypeRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Car type not found with slug: " + slug));
        return convertToDTO(carType);
    }

    @Override
    public Page<CarTypeDTO> getAllCarTypes(Pageable pageable) {
        Page<CarType> carTypes = carTypeRepository.findAll(pageable);
        return carTypes.map(this::convertToDTO);
    }

    @Override
    public List<CarTypeDTO> getActiveCarTypes() {
        List<CarType> carTypes = carTypeRepository.findByIsActiveTrue();
        return carTypes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CarTypeDTO toggleCarTypeStatus(Long id) {
        CarType carType = carTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car type not found with id: " + id));

        carType.setIsActive(!carType.getIsActive());
        CarType updatedCarType = carTypeRepository.save(carType);
        return convertToDTO(updatedCarType);
    }

    @Override
    public boolean isSlugExists(String slug) {
        return carTypeRepository.existsBySlug(slug);
    }

    @Override
    public long getTotalCarTypes() {
        return carTypeRepository.count();
    }

    @Override
    public List<CarTypeDTO> getCarTypesWithCarCount() {
        List<CarType> carTypes = carTypeRepository.findAll();
        return carTypes.stream()
                .map(carType -> {
                    CarTypeDTO dto = convertToDTO(carType);
                    // Add car count (this would need a custom query for efficiency)
                    long carCount = carRepository.countByCarType_CarTypeId(carType.getCarTypeId());
                    // You can add carCount field in CarTypeDTO if needed
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Helper method: Entity to DTO conversion
    private CarTypeDTO convertToDTO(CarType carType) {
        CarTypeDTO dto = new CarTypeDTO();
        dto.setCarTypeId(carType.getCarTypeId());
        dto.setCarCategoryName(carType.getCarCategoryName());
        dto.setSlug(carType.getSlug());
        dto.setIsActive(carType.getIsActive());
        dto.setCreatedAt(carType.getCreatedAt());
        dto.setUpdatedAt(carType.getUpdatedAt());
        return dto;
    }
}