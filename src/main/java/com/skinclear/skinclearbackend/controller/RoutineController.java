package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.RoutineDTO;
import com.skinclear.skinclearbackend.entity.Routine;
import com.skinclear.skinclearbackend.resource.Error;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.RoutineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://d2azwxcl0602xy.cloudfront.net, http://skin-clear-sites.s3-website-ap-southeast-1.amazonaws.com")
@RequestMapping("/api/v1/routine")
public class RoutineController {
    private final RoutineService routineService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping("/save")
    public ResponseEntity<GeneralResponse> saveRoutine(Principal principal,
                                                       @RequestBody RoutineDTO routineDTO){
        logger.info("request - saveRoutine | (URL: /api/v1/routine/save) | (Method: POST) ");
        try {
            routineService.saveRoutine(routineDTO, principal);
            logger.info("response - saveRoutine | (URL: /api/v1/routine/save) | (Method: POST) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - saveRoutine | (URL: /api/v1/routine/save) | (Method: POST) | (status: 500)");
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

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> getAllRoutines(Principal principal){
        logger.info("request - getAllRoutines | (URL: /api/v1/routine/all) | (Method: GET) ");
        try {
            List<Routine> routines = routineService.getAllRoutines(principal);
            logger.info("response - getAllRoutines | (URL: /api/v1/routine/all) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(routines)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - getAllRoutines | (URL: /api/v1/routine/all) | (Method: GET) | (status: 500)");
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

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getRoutine(@PathVariable Long id, Principal principal){
        logger.info("request - getRoutine | (URL: /api/v1/routine/{id}) | (Method: GET) ");
        try {
            Routine routine = routineService.getRoutine(id, principal);
            logger.info("response - getRoutine | (URL: /api/v1/routine/{id}) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(routine)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - getRoutine | (URL: /api/v1/routine/{id}) | (Method: GET) | (status: 500)");
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

    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse> updateRoutine(@PathVariable Long id, Principal principal,
                                                         @RequestBody RoutineDTO routineDTO){
        logger.info("request - updateRoutine | (URL: /api/v1/routine/update/{id}) | (Method: PUT) ");
        try {
            routineService.updateRoutine(id, routineDTO, principal);
            logger.info("response - updateRoutine | (URL: /api/v1/routine/update/{id}) | (Method: PUT) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - updateRoutine | (URL: /api/v1/routine/update/{id}) | (Method: PUT) | (status: 500)");
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse> deleteRoutine(@PathVariable Long id, Principal principal){
        logger.info("request - deleteRoutine | (URL: /api/v1/routine/delete/{id}) | (Method: DELETE) ");
        try {
            routineService.deleteRoutine(id, principal);
            logger.info("response - deleteRoutine | (URL: /api/v1/routine/delete/{id}) | (Method: DELETE) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - deleteRoutine | (URL: /api/v1/routine/delete/{id}) | (Method: DELETE) | (status: 500)");
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

    @PostMapping("/product/{id}")
    public ResponseEntity<GeneralResponse> addProduct(@PathVariable Long id, Principal principal,
                                                      @RequestParam Long productId,
                                                      @RequestParam String type){
        logger.info("request - addProduct | (URL: /api/v1/routine/product/{id}) | (Method: POST) ");
        try {
            routineService.addProduct(id, productId, type, principal);
            logger.info("response - addProduct | (URL: /api/v1/routine/product/{id}) | (Method: POST) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - addProduct | (URL: /api/v1/routine/product/{id}) | (Method: POST) | (status: 500)");
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

    @DeleteMapping("/product/{id}")
    public ResponseEntity<GeneralResponse> removeProduct(@PathVariable Long id, Principal principal,
                                                         @RequestParam Long productId,
                                                         @RequestParam String type){
        logger.info("request - removeProduct | (URL: /api/v1/routine/product/{id}) | (Method: DELETE) ");
        try {
            routineService.removeProduct(id, productId, type, principal);
            logger.info("response - removeProduct | (URL: /api/v1/routine/product/{id}) | (Method: DELETE) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - removeProduct | (URL: /api/v1/routine/product/{id}) | (Method: DELETE) | (status: 500)");
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
