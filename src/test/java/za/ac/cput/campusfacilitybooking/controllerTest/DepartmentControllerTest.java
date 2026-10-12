package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.DepartmentController;
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.factory.DepartmentFactory;
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
        controller = new DepartmentController(service);

        department = DepartmentFactory.createDepartment(
                "D001",
                "Information Technology",
                "Block A",
                "Mr Adams"
        );
    }

    @Test
    void testCreate() {
        when(service.create(department)).thenReturn(department);

        ResponseEntity<Department> response = controller.create(department);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("D001", response.getBody().getDepartmentId());
    }

    @Test
    void testRead() {
        when(service.read("D001")).thenReturn(department);

        ResponseEntity<Department> response = controller.read("D001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Information Technology", response.getBody().getName());
    }

    @Test
    void testReadNotFound() {
        when(service.read("D999")).thenReturn(null);

        ResponseEntity<Department> response = controller.read("D999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        when(service.update(department)).thenReturn(department);

        ResponseEntity<Department> response = controller.update(department);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testDelete() {
        when(service.delete("D001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("D001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("D001");
    }
}