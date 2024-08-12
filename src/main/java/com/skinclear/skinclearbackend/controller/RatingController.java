package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.resource.Error;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.resource.RatingResource;
import com.skinclear.skinclearbackend.service.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/rating")
public class RatingController {

    private static final Logger logger = LoggerFactory.getLogger(RatingController.class);
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/rate")
    public ResponseEntity<GeneralResponse> rate(Principal principal, @RequestParam("rating") int rating) {
        try {
            logger.info("request - rate | (URL: /api/v1/rating/rate) | (Method: POST) ");
            ratingService.rate(principal, rating);
            logger.info("response - rate | (URL: /api/v1/rating/rate) | (Method: POST) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Rating saved successfully")
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - rate | (URL: /api/v1/rating/rate) | (Method: POST) | (status: 500)");
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

    @GetMapping("/rate")
    public ResponseEntity<GeneralResponse> getRating(Principal principal) {
        try {
            logger.info("request - getRating | (URL: /api/v1/rating/rate) | (Method: GET) ");
            List<RatingResource> ratings = ratingService.getRating(principal);
            logger.info("response - getRating | (URL: /api/v1/rating/rate) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Rating retrieved successfully")
                            .data(ratings)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - getRating | (URL: /api/v1/rating/rate) | (Method: GET) | (status: 500)");
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
