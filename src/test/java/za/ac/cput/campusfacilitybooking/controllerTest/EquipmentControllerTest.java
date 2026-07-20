package za.ac.cput.campusfacilitybooking.controllerTest;

/* Author: Angelia Van der Westhuizen 221420649
     Date: 19 July 2026 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.EquipmentController;
import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.service.EquipmentService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EquipmentControllerTest {

    private EquipmentService service;
    private EquipmentController controller;
    private Equipment equipment;

    @BeforeEach
    void setUp() {

        service = Mockito.mock(EquipmentService.class);
        controller = new EquipmentController(service);

        equipment = new Equipment.Builder()
                .setEquipmentId("EQUIP001")
                .setName("Projector")
                .setSerialNumber("SN67890")
                .setStatus("Available")
                .setFacility("Lab A")
                .build();
    }

    @Test
    void testCreate() {

        when(service.create(equipment)).thenReturn(equipment);

        ResponseEntity<Equipment> response = controller.create(equipment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EQUIP001", response.getBody().getEquipmentId());
    }

    @Test
    void testRead() {

        when(service.read("EQUIP001")).thenReturn(equipment);

        ResponseEntity<Equipment> response = controller.read("EQUIP001");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Projector", response.getBody().getName());
    }

    @Test
    void testUpdate() {

        when(service.update(equipment)).thenReturn(equipment);

        ResponseEntity<Equipment> response = controller.update(equipment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDelete() {

        when(service.delete("EQUIP001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("EQUIP001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}