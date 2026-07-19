package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.controller.StudentController;
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.domain.Student;
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

        controller = new StudentController();

        try {
            var field = StudentController.class.getDeclaredField("service");
            field.setAccessible(true);
            field.set(controller, service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Department department = new Department.Builder()
                .setDepartmentId("D001")
                .setName("IT")
                .setBuilding("Block A")
                .setHeadOfDepartment("Mr Adams")
                .build();

        student = new Student.Builder()
                .setStudentId("S001")
                .setFirstName("Jada")
                .setLastName("Rinquest")
                .setEmail("jada@gmail.com")
                .setStudentNumber("222871296")
                .setDepartment(department)
                .build();
    }

    @Test
    void create() {

        when(service.create(student)).thenReturn(student);

        Student created = controller.create(student);

        assertNotNull(created);
    }

    @Test
    void read() {

        when(service.read("S001")).thenReturn(student);

        Student found = controller.read("S001");

        assertNotNull(found);
    }

    @Test
    void update() {

        when(service.update(student)).thenReturn(student);

        Student updated = controller.update(student);

        assertNotNull(updated);
    }

    @Test
    void delete() {

        when(service.delete("S001")).thenReturn(true);

        boolean deleted = controller.delete("S001");

        assertTrue(deleted);
    }
}
