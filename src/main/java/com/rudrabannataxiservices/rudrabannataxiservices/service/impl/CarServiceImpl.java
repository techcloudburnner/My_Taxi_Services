package com.rudrabannataxiservices.rudrabannataxiservices.service.impl;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.CarDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.model.Car;
import com.rudrabannataxiservices.rudrabannataxiservices.model.CarType;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.CarRepository;
import com.rudrabannataxiservices.rudrabannataxiservices.repository.CarTypeRepository;
import com.rudrabannataxiservices.rudrabannataxiservices.service.CarService;
import com.rudrabannataxiservices.rudrabannataxiservices.service.FileStorageService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public CarDTO createCar(CarDTO carDTO) {
        validateCarDTO(carDTO);

        CarType carType = carTypeRepository.findById(carDTO.getCarTypeId())
                .orElseThrow(() -> new RuntimeException("Car type not found with id: " + carDTO.getCarTypeId()));

        Car car = new Car();
        car.setName(carDTO.getName());
        car.setSlug(carDTO.getSlug());
        car.setCarType(carType);
        car.setImagePath(carDTO.getImagePath());
        car.setSeatingCapacity(carDTO.getSeatingCapacity());
        car.setLuggageCapacity(carDTO.getLuggageCapacity());
        car.setPerKmRate(carDTO.getPerKmRate());
        car.setDescription(carDTO.getDescription());
        car.setIsFeatured(carDTO.getIsFeatured() != null ? carDTO.getIsFeatured() : false);
        car.setStatus(carDTO.getStatus() != null ? carDTO.getStatus() : "active");

        Car savedCar = carRepository.save(car);
        return convertToDTO(savedCar);
    }

    @Override
    public CarDTO createCarWithImage(CarDTO carDTO, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = fileStorageService.storeCarImage(imageFile);
            carDTO.setImagePath(imagePath);
        }
        return createCar(carDTO);
    }

    @Override
    public CarDTO updateCar(Long id, CarDTO carDTO) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

        if (carDTO.getName() != null) car.setName(carDTO.getName());
        if (carDTO.getSlug() != null) car.setSlug(carDTO.getSlug());
        // ✅ ONLY update imagePath if it's explicitly provided (not null)
        if (carDTO.getImagePath() != null && !carDTO.getImagePath().isEmpty()) {
            if (car.getImagePath() != null && !car.getImagePath().equals(carDTO.getImagePath())) {
                fileStorageService.deleteFile(car.getImagePath());
            }
            car.setImagePath(carDTO.getImagePath());
        }

        if (carDTO.getSeatingCapacity() != null) car.setSeatingCapacity(carDTO.getSeatingCapacity());
        if (carDTO.getLuggageCapacity() != null) car.setLuggageCapacity(carDTO.getLuggageCapacity());
        if (carDTO.getPerKmRate() != null) car.setPerKmRate(carDTO.getPerKmRate());
        if (carDTO.getDescription() != null) car.setDescription(carDTO.getDescription());
        if (carDTO.getIsFeatured() != null) car.setIsFeatured(carDTO.getIsFeatured());
        if (carDTO.getCarTypeId() != null) {
            CarType carType = carTypeRepository.findById(carDTO.getCarTypeId())
                    .orElseThrow(() -> new RuntimeException("Car type not found"));
            car.setCarType(carType);
        }
        if (carDTO.getStatus() != null) car.setStatus(carDTO.getStatus());


        Car updatedCar = carRepository.save(car);
        return convertToDTO(updatedCar);
    }

    @Override
    public CarDTO updateCarWithImage(Long id, CarDTO carDTO, MultipartFile imageFile) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

        // ✅ Only handle image if a new one is provided
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (car.getImagePath() != null) {
                fileStorageService.deleteFile(car.getImagePath());
            }
            // Store new image
            String newImagePath = fileStorageService.storeCarImage(imageFile);
            carDTO.setImagePath(newImagePath);
        } else {
            // ✅ If no new image, keep the existing image path
            carDTO.setImagePath(car.getImagePath());
        }

        return updateCar(id, carDTO);
    }

    @Override
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

        if (car.getImagePath() != null) {
            fileStorageService.deleteFile(car.getImagePath());
        }

        carRepository.deleteById(id);
    }

    @Override
    public CarDTO getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
        return convertToDTO(car);
    }

    @Override
    public CarDTO getCarBySlug(String slug) {
        Car car = carRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Car not found with slug: " + slug));
        return convertToDTO(car);
    }

    @Override
    public Page<CarDTO> getAllCars(Pageable pageable) {
        Page<Car> cars = carRepository.findAll(pageable);
        return cars.map(this::convertToDTO);
    }

    @Override
    public Page<CarDTO> getFilteredCars(Long carTypeId, Integer minSeats, BigDecimal minRate,
                                        BigDecimal maxRate, Boolean isFeatured, Pageable pageable) {

        Specification<Car> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (carTypeId != null) {
                predicates.add(criteriaBuilder.equal(root.get("carType").get("carTypeId"), carTypeId));
            }
            if (minSeats != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seatingCapacity"), minSeats));
            }
            if (minRate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("perKmRate"), minRate));
            }
            if (maxRate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("perKmRate"), maxRate));
            }
            if (isFeatured != null) {
                predicates.add(criteriaBuilder.equal(root.get("isFeatured"), isFeatured));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Car> cars = carRepository.findAll(spec, pageable);
        return cars.map(this::convertToDTO);
    }

    @Override
    public List<CarDTO> getFeaturedCars() {
        List<Car> cars = carRepository.findByIsFeaturedTrue();
        return cars.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CarDTO> getCarsByType(Long carTypeId) {
        List<Car> cars = carRepository.findByCarType_CarTypeId(carTypeId);
        return cars.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CarDTO> searchCars(String keyword) {
        List<Car> cars = carRepository.searchCarsByName(keyword);
        return cars.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CarDTO> getCarsByPriceRange(BigDecimal minRate, BigDecimal maxRate) {
        if (minRate == null || maxRate == null) {
            throw new RuntimeException("Min rate and max rate are required");
        }
        if (minRate.compareTo(maxRate) > 0) {
            throw new RuntimeException("Min rate cannot be greater than max rate");
        }
        List<Car> cars = carRepository.findByPerKmRateBetween(minRate, maxRate);
        return cars.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public long getTotalCars() {
        return carRepository.count();
    }

    private void validateCarDTO(CarDTO carDTO) {
        if (carDTO.getName() == null || carDTO.getName().trim().isEmpty()) {
            throw new RuntimeException("Car name is required");
        }
        if (carDTO.getCarTypeId() == null) {
            throw new RuntimeException("Car type is required");
        }
        if (carDTO.getPerKmRate() == null || carDTO.getPerKmRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Per km rate must be positive");
        }
        if (carDTO.getSeatingCapacity() == null || carDTO.getSeatingCapacity() < 1) {
            throw new RuntimeException("Seating capacity must be at least 1");
        }
    }

    private CarDTO convertToDTO(Car car) {
        CarDTO dto = new CarDTO();
        dto.setCarId(car.getCarId());
        dto.setCarTypeId(car.getCarType().getCarTypeId());
        dto.setCarTypeName(car.getCarType().getCarCategoryName());
        dto.setName(car.getName());
        dto.setSlug(car.getSlug());
        dto.setImagePath(car.getImagePath());
        dto.setSeatingCapacity(car.getSeatingCapacity());
        dto.setLuggageCapacity(car.getLuggageCapacity());
        dto.setPerKmRate(car.getPerKmRate());
        dto.setDescription(car.getDescription());
        dto.setIsFeatured(car.getIsFeatured());
        dto.setCreatedAt(car.getCreatedAt());
        dto.setUpdatedAt(car.getUpdatedAt());
        dto.setStatus(car.getStatus());

        return dto;
    }
}