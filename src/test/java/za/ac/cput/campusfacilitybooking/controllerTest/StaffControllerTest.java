package za.ac.cput.campusfacilitybooking.controllerTest;

/* Author: Milani Sani (230371574)
     Date: 19 July 2026 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.StaffController;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.factory.StaffFactory;
import za.ac.cput.campusfacilitybooking.service.StaffService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StaffControllerTest {

    private StaffService service;
    private StaffController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(StaffService.class);
        controller = new StaffController(service);
    }

    @Test
    void testCreate() {
        Staff staff = StaffFactory.createStaff(
                "S001",
                "U001"
        );

        when(service.create(staff)).thenReturn(staff);

        ResponseEntity<Staff> response = controller.create(staff);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(staff.getStaffId(), response.getBody().getStaffId());
    }

    @Test
    void testRead() {
        Staff staff = StaffFactory.createStaff(
                "S001",
                "U001"
        );

        when(service.read("S001")).thenReturn(staff);

        ResponseEntity<Staff> response = controller.read("S001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("S001", response.getBody().getStaffId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("S999")).thenReturn(null);

        ResponseEntity<Staff> response = controller.read("S999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Staff staff = StaffFactory.createStaff(
                "S001",
                "U002"
        );

        when(service.update(staff)).thenReturn(staff);

        ResponseEntity<Staff> response = controller.update(staff);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("U002", response.getBody().getUserId());
    }

    @Test
    void testDelete() {
        when(service.delete("S001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("S001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("S001");
    }
}