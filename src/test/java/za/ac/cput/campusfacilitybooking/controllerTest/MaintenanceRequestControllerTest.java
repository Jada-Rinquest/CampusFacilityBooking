package za.ac.cput.campusfacilitybooking.controllerTest;

/* Author: Milani Sani (230371574)
     Date: 19 July 2026 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.MaintenanceRequestController;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;
import za.ac.cput.campusfacilitybooking.factory.MaintenanceRequestFactory;
import za.ac.cput.campusfacilitybooking.service.MaintenanceRequestService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MaintenanceRequestControllerTest {

    private MaintenanceRequestService service;
    private MaintenanceRequestController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(MaintenanceRequestService.class);
        controller = new MaintenanceRequestController(service);
    }

    @Test
    void testCreate() {

        MaintenanceRequest maintenanceRequest = MaintenanceRequestFactory.createMaintenanceRequest(
                "MR001",
                "E001",
                "S001",
                "Projector bulb is burnt out",
                MaintenancePriority.HIGH,
                MaintenanceStatus.OPEN,
                LocalDate.of(2026, 7, 19)
        );

        when(service.create(maintenanceRequest)).thenReturn(maintenanceRequest);

        ResponseEntity<MaintenanceRequest> response = controller.create(maintenanceRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(maintenanceRequest.getRequestId(), response.getBody().getRequestId());
    }

    @Test
    void testRead() {

        MaintenanceRequest maintenanceRequest = MaintenanceRequestFactory.createMaintenanceRequest(
                "MR001",
                "E001",
                "S001",
                "Projector bulb is burnt out",
                MaintenancePriority.HIGH,
                MaintenanceStatus.OPEN,
                LocalDate.of(2026, 7, 19)
        );

        when(service.read(maintenanceRequest.getRequestId())).thenReturn(maintenanceRequest);

        ResponseEntity<MaintenanceRequest> response = controller.read(maintenanceRequest.getRequestId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(maintenanceRequest.getRequestId(), response.getBody().getRequestId());
    }

    @Test
    void testUpdate() {

        MaintenanceRequest maintenanceRequest = MaintenanceRequestFactory.createMaintenanceRequest(
                "MR001",
                "E001",
                "S001",
                "Projector bulb is burnt out - technician assigned",
                MaintenancePriority.HIGH,
                MaintenanceStatus.IN_PROGRESS,
                LocalDate.of(2026, 7, 19)
        );

        when(service.update(maintenanceRequest)).thenReturn(maintenanceRequest);

        ResponseEntity<MaintenanceRequest> response = controller.update(maintenanceRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(MaintenanceStatus.IN_PROGRESS, response.getBody().getStatus());
    }

    @Test
    void testDelete() {

        when(service.delete("MR001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("MR001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());

        verify(service).delete("MR001");
    }
}