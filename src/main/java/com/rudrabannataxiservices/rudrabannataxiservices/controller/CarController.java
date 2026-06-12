package com.rudrabannataxiservices.rudrabannataxiservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rudrabannataxiservices.rudrabannataxiservices.Appconstant.CarApiConstants;
import com.rudrabannataxiservices.rudrabannataxiservices.dto.CarDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(CarApiConstants.BASE_URL)
@CrossOrigin(origins = "*")
public class CarController {

    @Autowired
    private CarService carService;

    // Create Car (JSON)
    @PostMapping
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
        CarDTO createdCar = carService.createCar(carDTO);
        return new ResponseEntity<>(createdCar, HttpStatus.CREATED);
    }

    // Update Car (JSON)
    @PutMapping(CarApiConstants.BY_ID)
    public ResponseEntity<CarDTO> updateCar(@PathVariable Long id, @RequestBody CarDTO carDTO) {
        CarDTO updatedCar = carService.updateCar(id, carDTO);
        return ResponseEntity.ok(updatedCar);
    }

    // Delete Car
    @DeleteMapping(CarApiConstants.BY_ID)
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok(CarApiConstants.CAR_DELETED);
    }

    // Get Car By ID
    @GetMapping(CarApiConstants.BY_ID)
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id) {
        CarDTO car = carService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    // Get Car By Slug
    @GetMapping(CarApiConstants.BY_SLUG)
    public ResponseEntity<CarDTO> getCarBySlug(@PathVariable String slug) {
        CarDTO car = carService.getCarBySlug(slug);
        return ResponseEntity.ok(car);
    }

    // Get All Cars
    @GetMapping
    public ResponseEntity<Page<CarDTO>> getAllCars(
            @PageableDefault(size = 10, sort = "carId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CarDTO> cars = carService.getAllCars(pageable);
        return ResponseEntity.ok(cars);
    }

    // Filter Cars
    @GetMapping(CarApiConstants.FILTER)
    public ResponseEntity<Page<CarDTO>> getFilteredCars(
            @RequestParam(required = false) Long carTypeId,
            @RequestParam(required = false) Integer minSeats,
            @RequestParam(required = false) BigDecimal minRate,
            @RequestParam(required = false) BigDecimal maxRate,
            @RequestParam(required = false) Boolean isFeatured,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<CarDTO> cars = carService.getFilteredCars(carTypeId, minSeats, minRate, maxRate, isFeatured, pageable);
        return ResponseEntity.ok(cars);
    }

    // Get Featured Cars
    @GetMapping(CarApiConstants.FEATURED)
    public ResponseEntity<List<CarDTO>> getFeaturedCars() {
        List<CarDTO> cars = carService.getFeaturedCars();
        return ResponseEntity.ok(cars);
    }

    // Get Cars By Type
    @GetMapping(CarApiConstants.BY_TYPE)
    public ResponseEntity<List<CarDTO>> getCarsByType(@PathVariable Long carTypeId) {
        List<CarDTO> cars = carService.getCarsByType(carTypeId);
        return ResponseEntity.ok(cars);
    }

    // ✅ Create Car With Image
    @PostMapping(value = CarApiConstants.WITH_IMAGE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CarDTO> createCarWithImage(
            @RequestPart("car") String carJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        CarDTO carDTO = objectMapper.readValue(carJson, CarDTO.class);
        CarDTO createdCar = carService.createCarWithImage(carDTO, image);
        return new ResponseEntity<>(createdCar, HttpStatus.CREATED);
    }

    // ✅ Update Car With Image
    @PutMapping(value = CarApiConstants.UPDATE_WITH_IMAGE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CarDTO> updateCarWithImage(
            @PathVariable Long id,
            @RequestPart("car") String carJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        CarDTO carDTO = objectMapper.readValue(carJson, CarDTO.class);
        CarDTO updatedCar = carService.updateCarWithImage(id, carDTO, image);
        return ResponseEntity.ok(updatedCar);
    }

    // Search Cars
    @GetMapping(CarApiConstants.SEARCH)
    public ResponseEntity<List<CarDTO>> searchCars(@RequestParam String keyword) {
        List<CarDTO> cars = carService.searchCars(keyword);
        return ResponseEntity.ok(cars);
    }

    // Get Cars By Price Range
    @GetMapping(CarApiConstants.PRICE_RANGE)
    public ResponseEntity<List<CarDTO>> getCarsByPriceRange(@RequestParam BigDecimal minRate, @RequestParam BigDecimal maxRate) {
        List<CarDTO> cars = carService.getCarsByPriceRange(minRate, maxRate);
        return ResponseEntity.ok(cars);
    }

    // Get Total Cars Count
    @GetMapping(CarApiConstants.COUNT)
    public ResponseEntity<Long> getTotalCars() {
        long count = carService.getTotalCars();
        return ResponseEntity.ok(count);
    }

    // ========== STATUS RELATED ENDPOINTS ==========

    // Toggle status
    @PutMapping(CarApiConstants.TOGGLE_STATUS)  // ✅ CarApiConstants use karo
    public ResponseEntity<CarDTO> toggleCarStatus(@PathVariable Long id) {
        CarDTO car = carService.getCarById(id);

        if ("active".equals(car.getStatus())) {
            car.setStatus("deactive");
        } else {
            car.setStatus("active");
        }

        CarDTO updatedCar = carService.updateCar(id, car);
        return ResponseEntity.ok(updatedCar);
    }
//    @PutMapping("/{id}/toggle-status")
//    public ResponseEntity<CarDTO> toggleCarStatus(@PathVariable Long id) {
//        CarDTO car = carService.getCarById(id);
//
//        if ("active".equals(car.getStatus())) {
//            car.setStatus("deactive");
//        } else {
//            car.setStatus("active");
//        }
//
//        CarDTO updatedCar = carService.updateCar(id, car);
//        return ResponseEntity.ok(updatedCar);
//    }

    // Set specific status
//    @PutMapping("/{id}/status")
//    public ResponseEntity<CarDTO> setCarStatus(
//            @PathVariable Long id,
//            @RequestParam String status) {
//
//        if (!status.equals("active") && !status.equals("deactive")) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        CarDTO car = carService.getCarById(id);
//        car.setStatus(status);
//        CarDTO updatedCar = carService.updateCar(id, car);
//        return ResponseEntity.ok(updatedCar);
//    }

    @PutMapping(CarApiConstants.SET_STATUS)  // ✅ CarApiConstants use karo
    public ResponseEntity<CarDTO> setCarStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        if (!status.equals("active") && !status.equals("deactive")) {
            return ResponseEntity.badRequest().build();
        }

        CarDTO car = carService.getCarById(id);
        car.setStatus(status);
        CarDTO updatedCar = carService.updateCar(id, car);
        return ResponseEntity.ok(updatedCar);
    }
    // Get active cars only
    @GetMapping(CarApiConstants.ACTIVE_CARS)  // ✅ CarApiConstants use karo
    public ResponseEntity<List<CarDTO>> getActiveCars() {
        List<CarDTO> allCars = carService.getAllCars(Pageable.unpaged()).getContent();
        List<CarDTO> activeCars = allCars.stream()
                .filter(car -> "active".equals(car.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(activeCars);
    }
//    @GetMapping("/active")
//    public ResponseEntity<List<CarDTO>> getActiveCars() {
//        List<CarDTO> allCars = carService.getAllCars(Pageable.unpaged()).getContent();
//        List<CarDTO> activeCars = allCars.stream()
//                .filter(car -> "active".equals(car.getStatus()))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(activeCars);
//    }

}
//package com.rudrabannataxiservices.rudrabannataxiservices.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rudrabannataxiservices.rudrabannataxiservices.Appconstant.CarApiConstants;
//import com.rudrabannataxiservices.rudrabannataxiservices.dto.CarDTO;
//import com.rudrabannataxiservices.rudrabannataxiservices.service.CarService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@RestController
//@RequestMapping(CarApiConstants.BASE_URL)
//@CrossOrigin(origins = "*")
//public class CarController {
//
//    @Autowired
//    private CarService carService;
//
//    // Create Car (JSON)
//    @PostMapping
//    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
//        CarDTO createdCar = carService.createCar(carDTO);
//        return new ResponseEntity<>(createdCar, HttpStatus.CREATED);
//    }
//
//    // Update Car (JSON)
//    @PutMapping(CarApiConstants.BY_ID)
//    public ResponseEntity<CarDTO> updateCar(@PathVariable Long id, @RequestBody CarDTO carDTO) {
//        CarDTO updatedCar = carService.updateCar(id, carDTO);
//        return ResponseEntity.ok(updatedCar);
//    }
//
//    // Delete Car
//    @DeleteMapping(CarApiConstants.BY_ID)
//    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
//        carService.deleteCar(id);
//        return ResponseEntity.ok(CarApiConstants.CAR_DELETED);
//    }
//
//    // Get Car By ID
//    @GetMapping(CarApiConstants.BY_ID)
//    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id) {
//        CarDTO car = carService.getCarById(id);
//        return ResponseEntity.ok(car);
//    }
//
//    // Get Car By Slug
//    @GetMapping(CarApiConstants.BY_SLUG)
//    public ResponseEntity<CarDTO> getCarBySlug(@PathVariable String slug) {
//        CarDTO car = carService.getCarBySlug(slug);
//        return ResponseEntity.ok(car);
//    }
//
//    // Get All Cars
//    @GetMapping
//    public ResponseEntity<Page<CarDTO>> getAllCars(
//            @PageableDefault(size = 10, sort = "carId", direction = Sort.Direction.ASC) Pageable pageable) {
//        Page<CarDTO> cars = carService.getAllCars(pageable);
//        return ResponseEntity.ok(cars);
//    }
//
//    // Filter Cars
//    @GetMapping(CarApiConstants.FILTER)
//    public ResponseEntity<Page<CarDTO>> getFilteredCars(
//            @RequestParam(required = false) Long carTypeId,
//            @RequestParam(required = false) Integer minSeats,
//            @RequestParam(required = false) BigDecimal minRate,
//            @RequestParam(required = false) BigDecimal maxRate,
//            @RequestParam(required = false) Boolean isFeatured,
//            @PageableDefault(size = 10) Pageable pageable) {
//        Page<CarDTO> cars = carService.getFilteredCars(carTypeId, minSeats, minRate, maxRate, isFeatured, pageable);
//        return ResponseEntity.ok(cars);
//    }
//
//    // Get Featured Cars
//    @GetMapping(CarApiConstants.FEATURED)
//    public ResponseEntity<List<CarDTO>> getFeaturedCars() {
//        List<CarDTO> cars = carService.getFeaturedCars();
//        return ResponseEntity.ok(cars);
//    }
//
//    // Get Cars By Type
//    @GetMapping(CarApiConstants.BY_TYPE)
//    public ResponseEntity<List<CarDTO>> getCarsByType(@PathVariable Long carTypeId) {
//        List<CarDTO> cars = carService.getCarsByType(carTypeId);
//        return ResponseEntity.ok(cars);
//    }
//
//    // ✅ Create Car With Image
//    @PostMapping(value = CarApiConstants.WITH_IMAGE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<CarDTO> createCarWithImage(
//            @RequestPart("car") String carJson,
//            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        CarDTO carDTO = objectMapper.readValue(carJson, CarDTO.class);
//        CarDTO createdCar = carService.createCarWithImage(carDTO, image);
//        return new ResponseEntity<>(createdCar, HttpStatus.CREATED);
//    }
//
//    // ✅ Update Car With Image (ONLY ONE - removed duplicate!)
//    @PutMapping(value = CarApiConstants.UPDATE_WITH_IMAGE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<CarDTO> updateCarWithImage(
//            @PathVariable Long id,
//            @RequestPart("car") String carJson,
//            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        CarDTO carDTO = objectMapper.readValue(carJson, CarDTO.class);
//        CarDTO updatedCar = carService.updateCarWithImage(id, carDTO, image);
//        return ResponseEntity.ok(updatedCar);
//    }
//
//    // Search Cars
//    @GetMapping(CarApiConstants.SEARCH)
//    public ResponseEntity<List<CarDTO>> searchCars(@RequestParam String keyword) {
//        List<CarDTO> cars = carService.searchCars(keyword);
//        return ResponseEntity.ok(cars);
//    }
//
//    // Get Cars By Price Range
//    @GetMapping(CarApiConstants.PRICE_RANGE)
//    public ResponseEntity<List<CarDTO>> getCarsByPriceRange(@RequestParam BigDecimal minRate, @RequestParam BigDecimal maxRate) {
//        List<CarDTO> cars = carService.getCarsByPriceRange(minRate, maxRate);
//        return ResponseEntity.ok(cars);
//    }
//
//    // Get Total Cars Count
//    @GetMapping(CarApiConstants.COUNT)
//    public ResponseEntity<Long> getTotalCars() {
//        long count = carService.getTotalCars();
//        return ResponseEntity.ok(count);
//    }
//}