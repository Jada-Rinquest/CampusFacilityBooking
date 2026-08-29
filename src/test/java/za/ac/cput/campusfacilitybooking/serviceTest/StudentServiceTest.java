package za.ac.cput.campusfacilitybooking.serviceTest;

//Jada Rinquest 12/07/2026
//222871296

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Student;
import za.ac.cput.campusfacilitybooking.factory.StudentFactory;
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

        student = StudentFactory.createStudent(
                "S001",
                "222871296",
                "U001"
        );
    }

    @Test
    void testCreate() {
        when(repository.save(student)).thenReturn(student);

        Student created = service.create(student);

        assertNotNull(created);
        assertEquals("S001", created.getStudentId());
    }

    @Test
    void testRead() {
        when(repository.findById("S001"))
                .thenReturn(Optional.of(student));

        Student found = service.read("S001");

        assertNotNull(found);
        assertEquals("S001", found.getStudentId());
    }

    @Test
    void testUpdate() {
        Student updatedStudent = StudentFactory.createStudent(
                "S001",
                "222871297",
                "U001"
        );

        when(repository.save(updatedStudent)).thenReturn(updatedStudent);

        Student updated = service.update(updatedStudent);

        assertNotNull(updated);
        assertEquals("222871297", updated.getStudentNumber());
    }

    @Test
    void testDelete() {
        when(repository.existsById("S001")).thenReturn(true);

        boolean deleted = service.delete("S001");

        assertTrue(deleted);
        verify(repository).deleteById("S001");
    }
}