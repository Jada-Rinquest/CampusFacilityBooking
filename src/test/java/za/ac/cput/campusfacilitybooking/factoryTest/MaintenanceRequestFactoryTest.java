package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Milani Sani(230371574)
   Date: 28 June 2026 */

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.factory.MaintenanceRequestFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceRequestFactoryTest {

    @Test
    void testCreateMaintenanceRequest() {

        LocalDate today = LocalDate.of(2026, 6, 28);

        MaintenanceRequest request =
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR001",
                        "E001",
                        "S001",
                        "Projector bulb is burnt out",
                        today,
                        "MP001",
                        "MS001"
                );

        assertNotNull(request);
        assertEquals("MR001", request.getRequestId());
        assertEquals("E001", request.getEquipmentId());
        assertEquals("S001", request.getReportedBy());
        assertEquals("Projector bulb is burnt out", request.getDescription());
        assertEquals(today, request.getDateReported());
        assertEquals("MP001", request.getMaintenancePriorityId());
        assertEquals("MS001", request.getMaintenanceStatusId());
    }

    @Test
    void testCreateMaintenanceRequestWithInvalidRequestId() {

        assertThrows(IllegalArgumentException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "",
                        "E001",
                        "S001",
                        "Projector bulb is burnt out",
                        LocalDate.now(),
                        "MP001",
                        "MS001"
                )
        );
    }

    @Test
    void testCreateMaintenanceRequestWithInvalidEquipmentId() {

        assertThrows(IllegalArgumentException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR002",
                        "",
                        "S002",
                        "Aircon is making a noise",
                        LocalDate.now(),
                        "MP001",
                        "MS001"
                )
        );
    }

    @Test
    void testCreateMaintenanceRequestWithInvalidReportedBy() {

        assertThrows(IllegalArgumentException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR003",
                        "E001",
                        "",
                        "A chair is broken",
                        LocalDate.now(),
                        "MP001",
                        "MS001"
                )
        );
    }

    @Test
    void testCreateMaintenanceRequestWithInvalidDescription() {

        assertThrows(IllegalArgumentException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR004",
                        "E001",
                        "S004",
                        "",
                        LocalDate.now(),
                        "MP001",
                        "MS001"
                )
        );
    }

    @Test
    void testCreateMaintenanceRequestWithNullDate() {

        assertThrows(IllegalArgumentException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR005",
                        "E001",
                        "S005",
                        "Equipment is missing",
                        null,
                        "MP001",
                        "MS001"
                )
        );
    }

    @Test
    void testCreateMaintenanceRequestWithInvalidPriorityId() {

        assertThrows(IllegalArgumentException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR006",
                        "E001",
                        "S006",
                        "Projector is not working",
                        LocalDate.now(),
                        "",
                        "MS001"
                )
        );
    }

    @Test
    void testCreateMaintenanceRequestWithInvalidStatusId() {

        assertThrows(IllegalArgumentException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR007",
                        "E001",
                        "S007",
                        "Projector is not working",
                        LocalDate.now(),
                        "MP001",
                        ""
                )
        );
    }
}