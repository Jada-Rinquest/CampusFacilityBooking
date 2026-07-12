package za.ac.cput.campusfacilitybooking.serviceTest;

/* MaintenanceRequestServiceImplTest.java
   MaintenanceRequestServiceImpl Test class
   Author: Milani Sani (230371574)
   Date: 12 July 2026
*/

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;
import za.ac.cput.campusfacilitybooking.factory.MaintenanceRequestFactory;
import za.ac.cput.campusfacilitybooking.repository.EquipmentRepository;
import za.ac.cput.campusfacilitybooking.service.IMaintenanceRequestService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MaintenanceRequestServiceImplTest {

    @Autowired
    private IMaintenanceRequestService maintenanceRequestService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private MaintenanceRequest maintenanceRequest;
    private Equipment equipment;

    @BeforeEach
    void setUp() {
        equipment = new Equipment.Builder()
                .equipmentId("E001")
                .name("Projector")
                .serialNumber("SN-12345")
                .build();

        equipmentRepository.save(equipment);

        maintenanceRequest = MaintenanceRequestFactory.createMaintenanceRequest(
                "MR001",
                equipment,
                "S001",
                "Projector bulb burnt out",
                MaintenancePriority.HIGH,
                MaintenanceStatus.OPEN,
                LocalDate.of(2026, 7, 12));
    }

    @Test
    @Order(1)
    void create() {
        MaintenanceRequest created = maintenanceRequestService.create(maintenanceRequest);
        assertNotNull(created);
        assertEquals("MR001", created.getRequestId());
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void read() {
        maintenanceRequestService.create(maintenanceRequest);
        MaintenanceRequest found = maintenanceRequestService.read("MR001");
        assertNotNull(found);
        assertEquals("Projector bulb burnt out", found.getDescription());
        System.out.println("Read: " + found);
    }

    @Test
    @Order(3)
    void update() {
        maintenanceRequestService.create(maintenanceRequest);
        MaintenanceRequest updated = new MaintenanceRequest.Builder()
                .requestId("MR001")
                .equipment(equipment)
                .reportedById("S001")
                .description("Projector bulb burnt out - technician assigned")
                .priority(MaintenancePriority.HIGH)
                .status(MaintenanceStatus.IN_PROGRESS)
                .dateReported(LocalDate.of(2026, 7, 12))
                .build();
        MaintenanceRequest result = maintenanceRequestService.update(updated);
        assertNotNull(result);
        assertEquals(MaintenanceStatus.IN_PROGRESS, result.getStatus());
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void findByStatus() {
        maintenanceRequestService.create(maintenanceRequest);
        List<MaintenanceRequest> results = maintenanceRequestService.findByStatus(MaintenanceStatus.OPEN);
        assertFalse(results.isEmpty());
        System.out.println("Found by status: " + results);
    }

    @Test
    @Order(5)
    void findByPriority() {
        maintenanceRequestService.create(maintenanceRequest);
        List<MaintenanceRequest> results = maintenanceRequestService.findByPriority(MaintenancePriority.HIGH);
        assertFalse(results.isEmpty());
        System.out.println("Found by priority: " + results);
    }

    @Test
    @Order(6)
    void findByReportedById() {
        maintenanceRequestService.create(maintenanceRequest);
        List<MaintenanceRequest> results = maintenanceRequestService.findByReportedById("S001");
        assertFalse(results.isEmpty());
        System.out.println("Found by reportedById: " + results);
    }

    @Test
    @Order(7)
    void findByEquipmentId() {
        maintenanceRequestService.create(maintenanceRequest);
        List<MaintenanceRequest> results = maintenanceRequestService.findByEquipmentId("E001");
        assertFalse(results.isEmpty());
        System.out.println("Found by equipment ID: " + results);
    }

    @Test
    @Order(8)
    void getAll() {
        maintenanceRequestService.create(maintenanceRequest);
        Set<MaintenanceRequest> all = maintenanceRequestService.getAll();
        assertFalse(all.isEmpty());
        System.out.println("All maintenance requests: " + all);
    }

    @Test
    @Order(9)
    void delete() {
        maintenanceRequestService.create(maintenanceRequest);
        boolean deleted = maintenanceRequestService.delete("MR001");
        assertTrue(deleted);
        assertNull(maintenanceRequestService.read("MR001"));
        System.out.println("Successfully deleted MR001");
    }

    @Test
    @Order(10)
    void delete_nonExistentId_returnsFalse() {
        boolean result = maintenanceRequestService.delete("DOES_NOT_EXIST");
        assertFalse(result);
        System.out.println("Delete non-existent returned: " + result);
    }

    @Test
    @Order(11)
    void update_nonExistentRequest_returnsNull() {
        MaintenanceRequest ghost = MaintenanceRequestFactory.createMaintenanceRequest(
                "GHOST", equipment, "S001", "Ghost request",
                MaintenancePriority.LOW, MaintenanceStatus.OPEN, LocalDate.now());
        MaintenanceRequest result = maintenanceRequestService.update(ghost);
        assertNull(result);
        System.out.println("Update non-existent returned: " + result);
    }
}
