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
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;
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
                "Thandiwe",
                "Khumalo",
                "thandiwe.khumalo@cput.ac.za",
                StaffRole.LECTURER,
                null
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
                "Thandiwe",
                "Khumalo",
                "thandiwe.khumalo@cput.ac.za",
                StaffRole.LECTURER,
                null
        );

        when(service.read(staff.getStaffId())).thenReturn(staff);

        ResponseEntity<Staff> response = controller.read(staff.getStaffId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(staff.getStaffId(), response.getBody().getStaffId());
    }

    @Test
    void testUpdate() {

        Staff staff = StaffFactory.createStaff(
                "S001",
                "Thandiwe",
                "Khumalo",
                "thandiwe.khumalo@cput.ac.za",
                StaffRole.STAFF,
                null
        );

        when(service.update(staff)).thenReturn(staff);

        ResponseEntity<Staff> response = controller.update(staff);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(StaffRole.STAFF, response.getBody().getRole());
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
