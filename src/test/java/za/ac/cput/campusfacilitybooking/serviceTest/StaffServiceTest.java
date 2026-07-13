package za.ac.cput.campusfacilitybooking.serviceTest;

/*Author: Milani Sani(230371574)
Date: 12 July 2026
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;
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

        Department department = new Department.Builder()
                .setDepartmentId("D001")
                .setName("Information Technology")
                .setBuilding("Block A")
                .setHeadOfDepartment("Mr Adams")
                .build();

        staff = new Staff.Builder()
                .staffId("S001")
                .firstName("Thandiwe")
                .lastName("Khumalo")
                .email("thandiwe.khumalo@cput.ac.za")
                .role(StaffRole.LECTURER)
                .department(department)
                .build();
    }

    @Test
    void create() {

        when(repository.save(staff)).thenReturn(staff);

        Staff created = service.create(staff);

        assertNotNull(created);

        assertEquals("S001", created.getStaffId());
    }

    @Test
    void read() {

        when(repository.findById("S001"))
                .thenReturn(Optional.of(staff));

        Staff found = service.read("S001");

        assertNotNull(found);

        assertEquals("Thandiwe", found.getFirstName());
    }

    @Test
    void update() {

        when(repository.save(staff)).thenReturn(staff);

        Staff updated = service.update(staff);

        assertNotNull(updated);
    }

    @Test
    void delete() {

        when(repository.existsById("S001")).thenReturn(true);

        boolean deleted = service.delete("S001");

        assertTrue(deleted);

        verify(repository).deleteById("S001");
    }
}