package com.rudrabannataxiservices.rudrabannataxiservices.service;

import com.rudrabannataxiservices.rudrabannataxiservices.dto.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

public interface CarService {

    // Basic CRUD
    CarDTO createCar(CarDTO carDTO);
    CarDTO updateCar(Long id, CarDTO carDTO);
    void deleteCar(Long id);
    CarDTO getCarById(Long id);
    CarDTO getCarBySlug(String slug);
    Page<CarDTO> getAllCars(Pageable pageable);

    // With image upload
    CarDTO createCarWithImage(CarDTO carDTO, MultipartFile imageFile);
    CarDTO updateCarWithImage(Long id, CarDTO carDTO, MultipartFile imageFile);

    // Filtering methods
    Page<CarDTO> getFilteredCars(Long carTypeId, Integer minSeats, BigDecimal minRate,
                                 BigDecimal maxRate, Boolean isFeatured, Pageable pageable);

    List<CarDTO> getFeaturedCars();
    List<CarDTO> getCarsByType(Long carTypeId);
    List<CarDTO> searchCars(String keyword);
    List<CarDTO> getCarsByPriceRange(BigDecimal minRate, BigDecimal maxRate);
    long getTotalCars();


}


//package com.rudrabannataxiservices.rudrabannataxiservices.service;
//
//import com.rudrabannataxiservices.rudrabannataxiservices.dto.CarDTO;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import java.math.BigDecimal;
//import java.util.List;
//
//public interface CarService {
//
//    CarDTO createCar(CarDTO carDTO);
//    CarDTO updateCar(Long id, CarDTO carDTO);
//    void deleteCar(Long id);
//    CarDTO getCarById(Long id);
//    CarDTO getCarBySlug(String slug);
//    Page<CarDTO> getAllCars(Pageable pageable);
//
//    // Filtering methods
//    Page<CarDTO> getFilteredCars(Long carTypeId, Integer minSeats, BigDecimal minRate,
//                                 BigDecimal maxRate, Boolean isFeatured, Pageable pageable);
//
//    List<CarDTO> getFeaturedCars();
//    List<CarDTO> getCarsByType(Long carTypeId);
//    List<CarDTO> searchCars(String keyword);
//    Page<CarDTO> getCarsByPriceRange(BigDecimal minRate, BigDecimal maxRate, Pageable pageable);
//    long getTotalCars();
//    List<CarDTO> getCarsByPriceRange(BigDecimal minRate, BigDecimal maxRate);
//
//}