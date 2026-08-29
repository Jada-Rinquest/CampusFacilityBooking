package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Jada Rinquest
   Date: 12/07/2026
   Student Number: 222871296 */

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Student;
import za.ac.cput.campusfacilitybooking.factory.StudentFactory;

import static org.junit.jupiter.api.Assertions.*;

class StudentFactoryTest {

    @Test
    void testCreateStudent() {
        Student student = StudentFactory.createStudent(
                "S001",
                "230123",
                "U001"
        );

        assertNotNull(student);
        assertEquals("S001", student.getStudentId());
        assertEquals("230123", student.getStudentNumber());
        assertEquals("U001", student.getUserId());
    }

    @Test
    void testCreateStudentWithInvalidStudentId() {
        assertThrows(IllegalArgumentException.class, () ->
                StudentFactory.createStudent(
                        "",
                        "230123",
                        "U001"
                )
        );
    }

    @Test
    void testCreateStudentWithInvalidStudentNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                StudentFactory.createStudent(
                        "S002",
                        "",
                        "U001"
                )
        );
    }

    @Test
    void testCreateStudentWithNullStudentId() {
        assertThrows(IllegalArgumentException.class, () ->
                StudentFactory.createStudent(
                        null,
                        "230123",
                        "U001"
                )
        );
    }

    @Test
    void testCreateStudentWithNullUserId() {
        assertThrows(IllegalArgumentException.class, () ->
                StudentFactory.createStudent(
                        "S003",
                        "230123",
                        null
                )
        );
    }

    @Test
    void testCreateStudentWithEmptyUserId() {
        assertThrows(IllegalArgumentException.class, () ->
                StudentFactory.createStudent(
                        "S004",
                        "230123",
                        ""
                )
        );
    }
}