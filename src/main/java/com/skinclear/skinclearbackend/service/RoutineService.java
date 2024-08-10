package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.dto.RoutineDTO;
import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.entity.Routine;
import com.skinclear.skinclearbackend.entity.User;
import com.skinclear.skinclearbackend.repository.RoutineRepository;
import com.skinclear.skinclearbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public RoutineService(RoutineRepository routineRepository, UserRepository userRepository, ProductService productService) {
        this.routineRepository = routineRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    public void saveRoutine(RoutineDTO routineDTO, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Routine routine = new Routine();
        routine.setName(routineDTO.getName());
        routine.setType(routineDTO.getType());
        routine.setDescription(routineDTO.getDescription());
        routine.setUserId(user.getId());
        routineRepository.save(routine);
    }

    public List<Routine> getAllRoutines(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return routineRepository.findAllByUserId(user.getId());
    }

    public Routine getRoutine(Long id, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return routineRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Routine not found"));
    }

    public void updateRoutine(Long id, RoutineDTO routineDTO, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Routine routine = routineRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Routine not found"));
        routine.setName(routineDTO.getName());
        routine.setType(routineDTO.getType());
        routine.setDescription(routineDTO.getDescription());
        routineRepository.save(routine);
    }

    public void deleteRoutine(Long id, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Routine routine = routineRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Routine not found"));
        routineRepository.delete(routine);
    }

    public void addProduct(Long id, Long productId, String type, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Routine routine = routineRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Routine not found"));
        Product product = productService.getProductById(productId);
        if (type.equals("morning")) {
            if (routine.getMorningProducts().contains(product)) {
                throw new RuntimeException("Product already exists in the routine");
            }
            routine.getMorningProducts().add(product);
        } else {
            if (routine.getEveningProducts().contains(product)) {
                throw new RuntimeException("Product already exists in the routine");
            }
            routine.getEveningProducts().add(product);
        }
        routineRepository.save(routine);
    }

    public void removeProduct(Long id, Long productId, String type, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Routine routine = routineRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Routine not found"));
        Product product = productService.getProductById(productId);
        if (type.equals("morning")) {
            routine.getMorningProducts().remove(product);
        } else {
            routine.getEveningProducts().remove(product);
        }
        routineRepository.save(routine);
    }
}
