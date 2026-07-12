package za.ac.cput.campusfacilitybooking.serviceTest;

/*Author: Milani Sani(230371574)
Date: 12 July 2026
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Equipment;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;
import za.ac.cput.campusfacilitybooking.repository.MaintananceRequestRepository;
import za.ac.cput.campusfacilitybooking.service.MaintenanceRequestService;
import za.ac.cput.campusfacilitybooking.service.impl.MaintenanceRequestServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaintenanceRequestServiceTest {

    private MaintananceRequestRepository repository;
    private MaintenanceRequestService service;

    private MaintenanceRequest maintenanceRequest;
    private Equipment equipment;

    @BeforeEach
    void setUp() {

        repository = Mockito.mock(MaintananceRequestRepository.class);

        service = new MaintenanceRequestServiceImpl(repository);

        equipment = new Equipment.Builder()
                .setEquipmentId("E001")
                .setName("Projector")
                .setSerialNumber("SN-12345")
                .build();

        maintenanceRequest = new MaintenanceRequest.Builder()
                .requestId("MR001")
                .equipment(equipment)
                .reportedById("S001")
                .description("Projector bulb is burnt out")
                .priority(MaintenancePriority.HIGH)
                .status(MaintenanceStatus.OPEN)
                .dateReported(LocalDate.of(2026, 7, 12))
                .build();
    }

    @Test
    void create() {

        when(repository.save(maintenanceRequest)).thenReturn(maintenanceRequest);

        MaintenanceRequest created = service.create(maintenanceRequest);

        assertNotNull(created);

        assertEquals("MR001", created.getRequestId());
    }

    @Test
    void read() {

        when(repository.findById("MR001"))
                .thenReturn(Optional.of(maintenanceRequest));

        MaintenanceRequest found = service.read("MR001");

        assertNotNull(found);

        assertEquals("Projector bulb is burnt out", found.getDescription());
    }

    @Test
    void update() {

        when(repository.save(maintenanceRequest)).thenReturn(maintenanceRequest);

        MaintenanceRequest updated = service.update(maintenanceRequest);

        assertNotNull(updated);
    }

    @Test
    void delete() {

        when(repository.existsById("MR001")).thenReturn(true);

        boolean deleted = service.delete("MR001");

        assertTrue(deleted);

        verify(repository).deleteById("MR001");
    }
}