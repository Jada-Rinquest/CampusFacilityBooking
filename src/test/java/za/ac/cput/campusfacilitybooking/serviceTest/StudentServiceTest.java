//Jada Rinquest 12/07/2026
//222871296

package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.domain.Student;
import za.ac.cput.campusfacilitybooking.repository.StudentRepository;
import za.ac.cput.campusfacilitybooking.service.StudentService;
import za.ac.cput.campusfacilitybooking.service.impl.StudentServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    private StudentRepository repository;
    private StudentService service;

    private Student student;

    @BeforeEach
    void setUp() {

        repository = Mockito.mock(StudentRepository.class);

        service = new StudentServiceImpl(repository);

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

        when(repository.save(student)).thenReturn(student);

        Student created = service.create(student);

        assertNotNull(created);

        assertEquals("S001", created.getStudentId());
    }

    @Test
    void read() {

        when(repository.findById("S001"))
                .thenReturn(Optional.of(student));

        Student found = service.read("S001");

        assertNotNull(found);

        assertEquals("Jada", found.getFirstName());
    }

    @Test
    void update() {

        when(repository.save(student)).thenReturn(student);

        Student updated = service.update(student);

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