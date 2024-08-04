package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.BrandDTO;
import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.resource.Error;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.BrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/brand")
public class BrandController extends AbstractController{

    private final static Logger logger = LoggerFactory.getLogger(BrandController.class);
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllBrandsWithPagination(@RequestParam int page , @RequestParam int size) {
        logger.info("request - getAllBrandsWithPagination | (URL: /api/v1/brand) | (Method: GET) | (page: {}) | (size: {})", page, size);
        Object allBrandWithPagination = brandService.getAllBrandWithPagination(page, size);
        logger.info("response - getAllBrandsWithPagination | (URL: /api/v1/brand) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(allBrandWithPagination)
                        .build()
        );
    }
    @GetMapping("/brandList")
    public ResponseEntity<GeneralResponse> getAllBrands(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam boolean isCrueltyFree,
            @RequestParam(required = false) String country) {
        logger.info("request - getAllBrandsWithPagination | (URL: /api/v1/brand) | (Method: GET) | (page: {}) | (size: {})", page, size);
        Object allBrandWithPagination = brandService.getAllBrandWithPagination(page, size, isCrueltyFree, country);
        logger.info("response - getAllBrandsWithPagination | (URL: /api/v1/brand) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(allBrandWithPagination)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getBrandById(@PathVariable Long id) {
        logger.info("request - getBrandById | (URL: /api/v1/brand/{brandId}) | (Method: GET) | (brandId: {})", id);
        Brand brand = brandService.getBrandById(id);
        logger.info("response - getBrandById | (URL: /api/v1/brand/{brandId}) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(brand)
                        .build()
        );

    }

    @GetMapping("/search")
    public ResponseEntity<GeneralResponse> getBrandsByKeywords(@RequestParam String keyword) {
        logger.info("request - getBrandsByKeywords | (URL: /api/v1/brand/search) | (Method: GET) | (keyword: {})", keyword);
        List<Brand> brands = brandService.searchBrandsByName(keyword);
        logger.info("response - getBrandsByKeywords | (URL: /api/v1/brand/search) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(brands)
                        .build()
        );
    }

    @PostMapping("/add")
    public ResponseEntity<GeneralResponse> addBrand(@RequestBody BrandDTO brandDTO) {
        logger.info("request - addBrand | (URL: /api/v1/brand/add) | (Method: POST) | (brand: {})", brandDTO);
        brandService.addBrand(brandDTO);
        logger.info("response - addBrand | (URL: /api/v1/brand/add) | (Method: POST) | (status: 201)");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Brand added successfully")
                            .build()
            );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse> updateBrand(@RequestBody Brand brand, @PathVariable Long id) {
        try {
            logger.info("request - updateBrand | (URL: /api/v1/brand/update/{brandId}) | (Method: PUT) | (brand: {}) | (brandId: {})", brand, id);
            brandService.updateBrand(brand, id);
            logger.info("response - updateBrand | (URL: /api/v1/brand/update/{brandId}) | (Method: PUT) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Brand updated successfully")
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - updateBrand | (URL: /api/v1/brand/update/{brandId}) | (Method: PUT) | (status: 500)");
            return ResponseEntity.status(500).body(
                    GeneralResponse.builder()
                            .success(false)
                            .error(Error.builder()
                                    .message(e.getMessage())
                                    .build()
                            )
                            .build()
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GeneralResponse> deleteBrand(@RequestBody List<Long> ids) {
        try {
            logger.info("request - deleteBrand | (URL: /api/v1/brand/delete) | (Method: DELETE) | (ids: {})", ids);
            brandService.deleteBrand(ids);
            logger.info("response - deleteBrand | (URL: /api/v1/brand/delete) | (Method: DELETE) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Brand deleted successfully")
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - deleteBrand | (URL: /api/v1/brand/delete) | (Method: DELETE) | (status: 500)");
            return ResponseEntity.status(500).body(
                    GeneralResponse.builder()
                            .success(false)
                            .error(Error.builder()
                                    .message(e.getMessage())
                                    .build()
                            )
                            .build()
            );
        }
    }

}
