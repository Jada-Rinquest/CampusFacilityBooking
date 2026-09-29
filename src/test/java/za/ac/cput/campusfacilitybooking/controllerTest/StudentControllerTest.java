package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.StudentController;
import za.ac.cput.campusfacilitybooking.domain.Student;
import za.ac.cput.campusfacilitybooking.factory.StudentFactory;
import za.ac.cput.campusfacilitybooking.service.StudentService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    private StudentService service;
    private StudentController controller;
    private Student student;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(StudentService.class);
        controller = new StudentController(service);

        student = StudentFactory.createStudent(
                "S001",
                "222871296",
                "U001"
        );
    }

    @Test
    void testCreate() {
        when(service.create(student)).thenReturn(student);

        ResponseEntity<Student> response = controller.create(student);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("S001", response.getBody().getStudentId());
    }

    @Test
    void testRead() {
        when(service.read("S001")).thenReturn(student);

        ResponseEntity<Student> response = controller.read("S001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("S001", response.getBody().getStudentId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("S999")).thenReturn(null);

        ResponseEntity<Student> response = controller.read("S999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Student updatedStudent = StudentFactory.createStudent(
                "S001",
                "222871297",
                "U001"
        );

        when(service.update(updatedStudent)).thenReturn(updatedStudent);

        ResponseEntity<Student> response = controller.update(updatedStudent);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("222871297", response.getBody().getStudentNumber());
    }

    @Test
    void testDelete() {
        when(service.delete("S001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("S001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("S001");
    }
}