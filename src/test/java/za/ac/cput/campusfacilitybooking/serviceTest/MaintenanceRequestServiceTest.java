package za.ac.cput.campusfacilitybooking.serviceTest;

/*Author: Milani Sani(230371574)
Date: 12 July 2026
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.MaintenanceRequest;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenancePriority;
import za.ac.cput.campusfacilitybooking.domain.enums.MaintenanceStatus;
import za.ac.cput.campusfacilitybooking.factory.MaintenanceRequestFactory;
import za.ac.cput.campusfacilitybooking.repository.MaintenanceRequestRepository;
import za.ac.cput.campusfacilitybooking.service.MaintenanceRequestService;
import za.ac.cput.campusfacilitybooking.service.impl.MaintenanceRequestServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaintenanceRequestServiceTest {

    private MaintenanceRequestRepository repository;
    private MaintenanceRequestService service;
    private MaintenanceRequest maintenanceRequest;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(MaintenanceRequestRepository.class);
        service = new MaintenanceRequestServiceImpl(repository);

        maintenanceRequest = MaintenanceRequestFactory.createMaintenanceRequest(
                "MR001",
                "E001",
                "S001",
                "Projector bulb is burnt out",
                LocalDate.of(2026, 7, 12),
                MaintenancePriority.HIGH,
                MaintenanceStatus.OPEN
        );
    }

    @Test
    void testCreate() {
        when(repository.save(maintenanceRequest)).thenReturn(maintenanceRequest);

        MaintenanceRequest created = service.create(maintenanceRequest);

        assertNotNull(created);
        assertEquals("MR001", created.getRequestId());
    }

    @Test
    void testRead() {
        when(repository.findById("MR001"))
                .thenReturn(Optional.of(maintenanceRequest));

        MaintenanceRequest found = service.read("MR001");

        assertNotNull(found);
        assertEquals("Projector bulb is burnt out", found.getDescription());
    }

    @Test
    void testUpdate() {
        when(repository.save(maintenanceRequest)).thenReturn(maintenanceRequest);

        MaintenanceRequest updated = service.update(maintenanceRequest);

        assertNotNull(updated);
    }

    @Test
    void testDelete() {
        when(repository.existsById("MR001")).thenReturn(true);

        boolean deleted = service.delete("MR001");

        assertTrue(deleted);
        verify(repository).deleteById("MR001");
    }
}