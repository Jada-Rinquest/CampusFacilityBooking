package za.ac.cput.campusfacilitybooking.serviceTest;

/*Author: Milani Sani(230371574)
Date: 12 July 2026
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.factory.StaffFactory;
import za.ac.cput.campusfacilitybooking.repository.StaffRepository;
import za.ac.cput.campusfacilitybooking.service.StaffService;
import za.ac.cput.campusfacilitybooking.service.impl.StaffServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffServiceTest {

    private StaffRepository repository;
    private StaffService service;
    private Staff staff;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(StaffRepository.class);
        service = new StaffServiceImpl(repository);

        staff = StaffFactory.createStaff(
                "S001",
                "U001"
        );
    }

    @Test
    void testCreate() {
        when(repository.save(staff)).thenReturn(staff);

        Staff created = service.create(staff);

        assertNotNull(created);
        assertEquals("S001", created.getStaffId());
    }

    @Test
    void testRead() {
        when(repository.findById("S001"))
                .thenReturn(Optional.of(staff));

        Staff found = service.read("S001");

        assertNotNull(found);
        assertEquals("S001", found.getStaffId());
    }

    @Test
    void testUpdate() {
        Staff updatedStaff = StaffFactory.createStaff(
                "S001",
                "U002"
        );

        when(repository.save(updatedStaff)).thenReturn(updatedStaff);

        Staff updated = service.update(updatedStaff);

        assertNotNull(updated);
        assertEquals("U002", updated.getUserId());
    }

    @Test
    void testDelete() {
        when(repository.existsById("S001")).thenReturn(true);

        boolean deleted = service.delete("S001");

        assertTrue(deleted);
        verify(repository).deleteById("S001");
    }
}