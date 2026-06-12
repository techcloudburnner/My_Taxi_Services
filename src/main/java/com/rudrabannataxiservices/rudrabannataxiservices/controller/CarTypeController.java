package com.rudrabannataxiservices.rudrabannataxiservices.controller;

import com.rudrabannataxiservices.rudrabannataxiservices.Appconstant.CarTypeApiConstants;
import com.rudrabannataxiservices.rudrabannataxiservices.dto.CarTypeDTO;
import com.rudrabannataxiservices.rudrabannataxiservices.service.CarTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CarTypeApiConstants.BASE_URL)
@CrossOrigin(origins = "*")
public class CarTypeController {

    @Autowired
    private CarTypeService carTypeService;

    // Create Car Type
    @PostMapping
    public ResponseEntity<CarTypeDTO> createCarType(
            @RequestBody CarTypeDTO carTypeDTO) {

        CarTypeDTO createdCarType =
                carTypeService.createCarType(carTypeDTO);

        return new ResponseEntity<>(createdCarType, HttpStatus.CREATED);
    }

    // Update Car Type
    @PutMapping(CarTypeApiConstants.BY_ID)
    public ResponseEntity<CarTypeDTO> updateCarType(
            @PathVariable Long id,
            @RequestBody CarTypeDTO carTypeDTO) {

        CarTypeDTO updatedCarType =
                carTypeService.updateCarType(id, carTypeDTO);

        return ResponseEntity.ok(updatedCarType);
    }

    // Delete Car Type
    @DeleteMapping(CarTypeApiConstants.BY_ID)
    public ResponseEntity<String> deleteCarType(@PathVariable Long id) {

        carTypeService.deleteCarType(id);

        return ResponseEntity.ok(
                CarTypeApiConstants.CAR_TYPE_DELETED
        );
    }

    // Get Car Type By ID
    @GetMapping(CarTypeApiConstants.BY_ID)
    public ResponseEntity<CarTypeDTO> getCarTypeById(
            @PathVariable Long id) {

        CarTypeDTO carType =
                carTypeService.getCarTypeById(id);

        return ResponseEntity.ok(carType);
    }

    // Get Car Type By Slug
    @GetMapping(CarTypeApiConstants.BY_SLUG)
    public ResponseEntity<CarTypeDTO> getCarTypeBySlug(
            @PathVariable String slug) {

        CarTypeDTO carType =
                carTypeService.getCarTypeBySlug(slug);

        return ResponseEntity.ok(carType);
    }

    // Get All Car Types
    @GetMapping
    public ResponseEntity<Page<CarTypeDTO>> getAllCarTypes(
            @PageableDefault(
                    size = 10,
                    sort = "carTypeId",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {

        Page<CarTypeDTO> carTypes =
                carTypeService.getAllCarTypes(pageable);

        return ResponseEntity.ok(carTypes);
    }

    // Get Active Car Types
    @GetMapping(CarTypeApiConstants.ACTIVE)
    public ResponseEntity<List<CarTypeDTO>> getActiveCarTypes() {

        List<CarTypeDTO> carTypes =
                carTypeService.getActiveCarTypes();

        return ResponseEntity.ok(carTypes);
    }

    // Toggle Car Type Status
    @PatchMapping(CarTypeApiConstants.TOGGLE_STATUS)
    public ResponseEntity<CarTypeDTO> toggleCarTypeStatus(
            @PathVariable Long id) {

        CarTypeDTO carType =
                carTypeService.toggleCarTypeStatus(id);

        return ResponseEntity.ok(carType);
    }

    // Check Slug Exists
    @GetMapping(CarTypeApiConstants.CHECK_SLUG)
    public ResponseEntity<Boolean> checkSlug(
            @RequestParam String slug) {

        boolean exists =
                carTypeService.isSlugExists(slug);

        return ResponseEntity.ok(exists);
    }

    // Get Total Car Types Count
    @GetMapping(CarTypeApiConstants.COUNT)
    public ResponseEntity<Long> getTotalCarTypes() {

        long count =
                carTypeService.getTotalCarTypes();

        return ResponseEntity.ok(count);
    }

    // Get Car Types With Car Count
    @GetMapping(CarTypeApiConstants.WITH_CAR_COUNT)
    public ResponseEntity<List<CarTypeDTO>> getCarTypesWithCarCount() {

        List<CarTypeDTO> carTypes =
                carTypeService.getCarTypesWithCarCount();

        return ResponseEntity.ok(carTypes);
    }
}