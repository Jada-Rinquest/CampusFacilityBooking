//Jada Rinquest 12/07/2026
//222871296

package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.repository.DepartmentRepository;
import za.ac.cput.campusfacilitybooking.service.DepartmentService;
import za.ac.cput.campusfacilitybooking.service.impl.DepartmentServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DepartmentServiceTest {

    private DepartmentRepository repository;
    private DepartmentService service;
    private Department department;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(DepartmentRepository.class);
        service = new DepartmentServiceImpl(repository);

        department = new Department.Builder()
                .setDepartmentId("D001")
                .setName("Information Technology")
                .setBuilding("Block A")
                .setHeadOfDepartment("Mr Adams")
                .build();
    }

    @Test
    void create() {
        when(repository.save(department)).thenReturn(department);

        Department created = service.create(department);

        assertNotNull(created);
        assertEquals("D001", created.getDepartmentId());
    }

    @Test
    void read() {
        when(repository.findById("D001"))
                .thenReturn(Optional.of(department));

        Department found = service.read("D001");

        assertNotNull(found);
        assertEquals("Information Technology", found.getName());
    }

    @Test
    void update() {
        when(repository.save(department)).thenReturn(department);

        Department updated = service.update(department);

        assertNotNull(updated);
    }

    @Test
    void delete() {

        when(repository.existsById("D001")).thenReturn(true);

        boolean deleted = service.delete("D001");

        assertTrue(deleted);

        verify(repository).deleteById("D001");
    }
}