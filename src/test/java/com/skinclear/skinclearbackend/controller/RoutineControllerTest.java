package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.RoutineDTO;
import com.skinclear.skinclearbackend.entity.Routine;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.RoutineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoutineControllerTest {

    @Mock
    private RoutineService routineService;

    @Mock
    private Principal principal;

    @InjectMocks
    private RoutineController routineController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRoutine() {
        RoutineDTO routineDTO = new RoutineDTO();
        doNothing().when(routineService).saveRoutine(routineDTO, principal);

        ResponseEntity<GeneralResponse> response = routineController.saveRoutine(principal, routineDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(routineService, times(1)).saveRoutine(routineDTO, principal);
    }

    @Test
    void testGetAllRoutines() {
        List<Routine> routines = Arrays.asList(new Routine(), new Routine());
        when(routineService.getAllRoutines(principal)).thenReturn(routines);

        ResponseEntity<GeneralResponse> response = routineController.getAllRoutines(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(routines, response.getBody().getData());
        verify(routineService, times(1)).getAllRoutines(principal);
    }

    @Test
    void testGetRoutine() {
        Long id = 1L;
        Routine routine = new Routine();
        when(routineService.getRoutine(id, principal)).thenReturn(routine);

        ResponseEntity<GeneralResponse> response = routineController.getRoutine(id, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(routine, response.getBody().getData());
        verify(routineService, times(1)).getRoutine(id, principal);
    }

    @Test
    void testUpdateRoutine() {
        Long id = 1L;
        RoutineDTO routineDTO = new RoutineDTO();
        doNothing().when(routineService).updateRoutine(id, routineDTO, principal);

        ResponseEntity<GeneralResponse> response = routineController.updateRoutine(id, principal, routineDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(routineService, times(1)).updateRoutine(id, routineDTO, principal);
    }

    @Test
    void testDeleteRoutine() {
        Long id = 1L;
        doNothing().when(routineService).deleteRoutine(id, principal);

        ResponseEntity<GeneralResponse> response = routineController.deleteRoutine(id, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(routineService, times(1)).deleteRoutine(id, principal);
    }

    @Test
    void testAddProduct() {
        Long routineId = 1L;
        Long productId = 2L;
        String type = "type";

        doNothing().when(routineService).addProduct(routineId, productId, type, principal);

        ResponseEntity<GeneralResponse> response = routineController.addProduct(routineId, principal, productId, type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(routineService, times(1)).addProduct(routineId, productId, type, principal);
    }

    @Test
    void testRemoveProduct() {
        Long routineId = 1L;
        Long productId = 2L;
        String type = "type";

        doNothing().when(routineService).removeProduct(routineId, productId, type, principal);

        ResponseEntity<GeneralResponse> response = routineController.removeProduct(routineId, principal, productId, type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(routineService, times(1)).removeProduct(routineId, productId, type, principal);
    }
}
