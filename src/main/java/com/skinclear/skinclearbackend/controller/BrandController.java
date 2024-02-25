package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/brand")
public class BrandController extends AbstractController{

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllBrandsWithPagination(@RequestParam int page ,@RequestParam int size) {
        return sendSuccessResponse(brandService.getAllBrandWithPagination(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBrandById(@PathVariable Long id) {
        return sendSuccessResponse(brandService.getBrandById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBrand(@RequestBody Brand brand) {
        boolean isAdded = brandService.addBrand(brand);
        if (isAdded) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Brand added successfully");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Brand already exists");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBrand(@RequestBody Brand brand, @PathVariable Long id) {
        String message = brandService.updateBrand(brand, id);
        if (message.equals("Brand updated successfully")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteBrand(@RequestBody List<Long> ids) {
        boolean isDeleted = brandService.deleteBrand(ids);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brand does not exist");
    }

}
