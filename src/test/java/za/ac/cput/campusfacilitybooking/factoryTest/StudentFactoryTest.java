package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;

import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.domain.Student;
import za.ac.cput.campusfacilitybooking.factory.DepartmentFactory;
import za.ac.cput.campusfacilitybooking.factory.StudentFactory;

import static org.junit.jupiter.api.Assertions.*;

class StudentFactoryTest {

    @Test
    void createStudent() {

        Department department =
                DepartmentFactory.createDepartment(
                        "D001",
                        "IT",
                        "Block A",
                        "Mr Adams"
                );

        Student student =
                StudentFactory.createStudent(
                        "S001",
                        "Jada",
                        "Smith",
                        "jada@gmail.com",
                        "230123",
                        department
                );

        assertNotNull(student);

        assertEquals(
                "Jada",
                student.getFirstName()
        );
    }
}