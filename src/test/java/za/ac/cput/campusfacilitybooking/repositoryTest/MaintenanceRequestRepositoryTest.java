package za.ac.cput.campusfacilitybooking.repositoryTest;

/* MaintenanceRequestRepositoryTest.java
   MaintenanceRequestRepository Test class
   Author: Milani Sani (230371574)
   Date: 21 June 2026
*/

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;
import za.ac.cput.campusfacilitybooking.factory.MaintenanceRequestFactory;
import za.ac.cput.campusfacilitybooking.repository.MaintenanceRequestRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MaintenanceRequestRepositoryTest {

    @Autowired
    private MaintenanceRequestRepository maintenanceRequestRepository;

    @Autowired
    private za.ac.cput.campusfacilitybooking.repository.EquipmentRepository equipmentRepository;

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
                LocalDate.of(2026, 6, 21));
    }

    @Test
    void save() {
        MaintenanceRequest saved = maintenanceRequestRepository.save(maintenanceRequest);
        assertNotNull(saved);
        assertEquals("MR001", saved.getRequestId());
        System.out.println("Saved: " + saved);
    }

    @Test
    void findById() {
        maintenanceRequestRepository.save(maintenanceRequest);
        Optional<MaintenanceRequest> found = maintenanceRequestRepository.findById("MR001");
        assertTrue(found.isPresent());
        assertEquals("Projector bulb burnt out", found.get().getDescription());
        System.out.println("Found by ID: " + found.get());
    }

    @Test
    void findByStatus() {
        maintenanceRequestRepository.save(maintenanceRequest);
        List<MaintenanceRequest> results = maintenanceRequestRepository.findByStatus(MaintenanceStatus.OPEN);
        assertFalse(results.isEmpty());
        System.out.println("Found by status: " + results);
    }

    @Test
    void findByPriority() {
        maintenanceRequestRepository.save(maintenanceRequest);
        List<MaintenanceRequest> results = maintenanceRequestRepository.findByPriority(MaintenancePriority.HIGH);
        assertFalse(results.isEmpty());
        System.out.println("Found by priority: " + results);
    }

    @Test
    void findByReportedById() {
        maintenanceRequestRepository.save(maintenanceRequest);
        List<MaintenanceRequest> results = maintenanceRequestRepository.findByReportedById("S001");
        assertFalse(results.isEmpty());
        System.out.println("Found by reportedById: " + results);
    }

    @Test
    void findByEquipmentEquipmentId() {
        maintenanceRequestRepository.save(maintenanceRequest);
        List<MaintenanceRequest> results = maintenanceRequestRepository.findByEquipmentEquipmentId("E001");
        assertFalse(results.isEmpty());
        System.out.println("Found by equipment ID: " + results);
    }

    @Test
    void update() {
        maintenanceRequestRepository.save(maintenanceRequest);
        MaintenanceRequest updated = new MaintenanceRequest.Builder()
                .requestId("MR001")
                .equipment(equipment)
                .reportedById("S001")
                .description("Projector bulb burnt out - under repair")
                .priority(MaintenancePriority.HIGH)
                .status(MaintenanceStatus.IN_PROGRESS)
                .dateReported(LocalDate.of(2026, 6, 21))
                .build();
        MaintenanceRequest saved = maintenanceRequestRepository.save(updated);
        assertEquals(MaintenanceStatus.IN_PROGRESS, saved.getStatus());
        System.out.println("Updated: " + saved);
    }

    @Test
    void delete() {
        maintenanceRequestRepository.save(maintenanceRequest);
        maintenanceRequestRepository.deleteById("MR001");
        Optional<MaintenanceRequest> deleted = maintenanceRequestRepository.findById("MR001");
        assertFalse(deleted.isPresent());
        System.out.println("Successfully deleted MaintenanceRequest with ID MR001");
    }

    @Test
    void findAll() {
        maintenanceRequestRepository.save(maintenanceRequest);
        List<MaintenanceRequest> all = maintenanceRequestRepository.findAll();
        assertFalse(all.isEmpty());
        System.out.println("All maintenance requests: " + all);
    }
}
