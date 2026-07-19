package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.controller.DepartmentController;
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.service.DepartmentService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentControllerTest {

    private DepartmentService service;
    private DepartmentController controller;
    private Department department;

    @BeforeEach
    void setUp() {

        service = Mockito.mock(DepartmentService.class);

        controller = new DepartmentController();

        try {
            var field = DepartmentController.class.getDeclaredField("service");
            field.setAccessible(true);
            field.set(controller, service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        department = new Department.Builder()
                .setDepartmentId("D001")
                .setName("IT")
                .setBuilding("Block A")
                .setHeadOfDepartment("Mr Adams")
                .build();
    }

    @Test
    void create() {

        when(service.create(department)).thenReturn(department);

        Department created = controller.create(department);

        assertNotNull(created);
        assertEquals("D001", created.getDepartmentId());
    }

    @Test
    void read() {

        when(service.read("D001")).thenReturn(department);

        Department found = controller.read("D001");

        assertNotNull(found);
    }

    @Test
    void update() {

        when(service.update(department)).thenReturn(department);

        Department updated = controller.update(department);

        assertNotNull(updated);
    }

    @Test
    void delete() {

        when(service.delete("D001")).thenReturn(true);

        boolean deleted = controller.delete("D001");

        assertTrue(deleted);
    }
}