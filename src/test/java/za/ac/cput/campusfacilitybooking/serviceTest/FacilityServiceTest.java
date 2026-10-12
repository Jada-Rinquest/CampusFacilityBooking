package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Facility;
import za.ac.cput.campusfacilitybooking.domain.enums.FacilityType;
import za.ac.cput.campusfacilitybooking.factory.FacilityFactory;
import za.ac.cput.campusfacilitybooking.repository.FacilityRepository;
import za.ac.cput.campusfacilitybooking.service.impl.FacilityServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FacilityServiceTest {

    private FacilityRepository repository;
    private FacilityServiceImpl service;
    private Facility facility;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(FacilityRepository.class);
        service = new FacilityServiceImpl(repository);

        facility = FacilityFactory.createFacility(
                "F001",
                "Computer Lab",
                40,
                "Block A",
                "D001",
                FacilityType.LAB
        );
    }

    @Test
    void testCreate() {
        when(repository.save(facility)).thenReturn(facility);

        Facility created = service.create(facility);

        assertNotNull(created);
        assertEquals("F001", created.getFacilityId());
    }

    @Test
    void testRead() {
        when(repository.findById("F001"))
                .thenReturn(Optional.of(facility));

        Facility found = service.read("F001");

        assertNotNull(found);
        assertEquals("Computer Lab", found.getName());
    }

    @Test
    void testUpdate() {
        when(repository.save(facility)).thenReturn(facility);

        Facility updated = service.update(facility);

        assertNotNull(updated);
    }

    @Test
    void testDelete() {
        when(repository.existsById("F001")).thenReturn(true);

        boolean deleted = service.delete("F001");

        assertTrue(deleted);
        verify(repository).deleteById("F001");
    }
}