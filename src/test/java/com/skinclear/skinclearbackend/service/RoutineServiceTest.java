package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.dto.RoutineDTO;
import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.entity.Routine;
import com.skinclear.skinclearbackend.entity.User;
import com.skinclear.skinclearbackend.repository.RoutineRepository;
import com.skinclear.skinclearbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoutineServiceTest {

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private RoutineService routineService;

    @Mock
    private Principal principal;

    private User user;
    private Routine routine;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        routine = new Routine();
        routine.setId(1L);
        routine.setName("Morning Routine");
        routine.setUserId(1L);

        product = new Product();
        product.setId(1L);

        when(principal.getName()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    @Test
    void testSaveRoutine() {
        RoutineDTO routineDTO = new RoutineDTO();
        routineDTO.setName("Morning Routine");
        routineDTO.setType("morning");
        routineDTO.setDescription("Routine description");

        routineService.saveRoutine(routineDTO, principal);

        ArgumentCaptor<Routine> routineArgumentCaptor = ArgumentCaptor.forClass(Routine.class);
        verify(routineRepository).save(routineArgumentCaptor.capture());

        Routine savedRoutine = routineArgumentCaptor.getValue();
        assertEquals("Morning Routine", savedRoutine.getName());
        assertEquals("morning", savedRoutine.getType());
        assertEquals("Routine description", savedRoutine.getDescription());
        assertEquals(user.getId(), savedRoutine.getUserId());
    }

    @Test
    void testGetAllRoutines() {
        when(routineRepository.findAllByUserId(user.getId())).thenReturn(Collections.singletonList(routine));

        List<Routine> routines = routineService.getAllRoutines(principal);

        assertEquals(1, routines.size());
        assertEquals(routine.getName(), routines.get(0).getName());
    }

    @Test
    void testGetRoutine() {
        when(routineRepository.findByIdAndUserId(routine.getId(), user.getId())).thenReturn(Optional.of(routine));

        Routine foundRoutine = routineService.getRoutine(routine.getId(), principal);

        assertEquals(routine.getName(), foundRoutine.getName());
    }

    @Test
    void testUpdateRoutine() {
        RoutineDTO routineDTO = new RoutineDTO();
        routineDTO.setName("Updated Routine");
        routineDTO.setType("evening");
        routineDTO.setDescription("Updated description");

        when(routineRepository.findByIdAndUserId(routine.getId(), user.getId())).thenReturn(Optional.of(routine));

        routineService.updateRoutine(routine.getId(), routineDTO, principal);

        ArgumentCaptor<Routine> routineArgumentCaptor = ArgumentCaptor.forClass(Routine.class);
        verify(routineRepository).save(routineArgumentCaptor.capture());

        Routine updatedRoutine = routineArgumentCaptor.getValue();
        assertEquals("Updated Routine", updatedRoutine.getName());
        assertEquals("evening", updatedRoutine.getType());
        assertEquals("Updated description", updatedRoutine.getDescription());
    }

    @Test
    void testDeleteRoutine() {
        when(routineRepository.findByIdAndUserId(routine.getId(), user.getId())).thenReturn(Optional.of(routine));

        routineService.deleteRoutine(routine.getId(), principal);

        verify(routineRepository).delete(routine);
    }

    @Test
    void testUserNotFoundException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> routineService.getAllRoutines(principal));
    }

    @Test
    void testRoutineNotFoundException() {
        when(routineRepository.findByIdAndUserId(routine.getId(), user.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> routineService.getRoutine(routine.getId(), principal));
    }
}
