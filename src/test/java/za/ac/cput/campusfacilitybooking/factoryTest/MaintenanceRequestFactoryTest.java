package za.ac.cput.campusfacilitybooking.factoryTest;

/*Author: Milani Sani(230371574)
Date: 28 June 2026
 */

import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.factory.MaintenanceRequestFactory;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceRequestFactoryTest {

    private Equipment buildEquipment() {
        return new Equipment.Builder()
                .equipmentId("E001")
                .name("Projector")
                .serialNumber("SN-12345")
                .build();
    }

    @Test
    void createMaintenanceRequest_validData_returnsMaintenanceRequest() {
        Equipment equipment = buildEquipment();
        LocalDate today = LocalDate.of(2026, 6, 28);

        MaintenanceRequest request = MaintenanceRequestFactory.createMaintenanceRequest(
                "MR001", equipment, "S001", "Projector bulb is burnt out",
                MaintenancePriority.HIGH, MaintenanceStatus.OPEN, today);

        assertNotNull(request);
        assertEquals("MR001", request.getRequestId());
        assertEquals(equipment, request.getEquipment());
        assertEquals("S001", request.getReportedById());
        assertEquals(MaintenancePriority.HIGH, request.getPriority());
        assertEquals(MaintenanceStatus.OPEN, request.getStatus());
        assertEquals(today, request.getDateReported());
    }

    @Test
    void createMaintenanceRequest_nullPriorityAndStatus_passThroughAsNull() {
        MaintenanceRequest request = MaintenanceRequestFactory.createMaintenanceRequest(
                "MR002", buildEquipment(), "S002", "Aircon is making a noise",
                null, null, null);

        assertNotNull(request);
        assertNull(request.getPriority());
        assertNull(request.getStatus());
    }

    @Test
    void createMaintenanceRequest_nullRequestId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        null, buildEquipment(), "S003", "Keyboard is not responding",
                        MaintenancePriority.LOW, MaintenanceStatus.OPEN, LocalDate.now()));
    }

    @Test
    void createMaintenanceRequest_nullEquipment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR003", null, "S004", "Equipment is missing",
                        MaintenancePriority.LOW, MaintenanceStatus.OPEN, LocalDate.now()));
    }

    @Test
    void createMaintenanceRequest_missingReportedById_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR005", buildEquipment(), null, "A Chair is broken",
                        MaintenancePriority.LOW, MaintenanceStatus.OPEN, LocalDate.now()));
    }

    @Test
    void createMaintenanceRequest_missingDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                MaintenanceRequestFactory.createMaintenanceRequest(
                        "MR006", buildEquipment(), "S006", null,
                        MaintenancePriority.LOW, MaintenanceStatus.OPEN, LocalDate.now()));
    }
}