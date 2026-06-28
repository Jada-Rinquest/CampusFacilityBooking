package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;

import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.factory.DepartmentFactory;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentFactoryTest {

    @Test
    void createDepartment() {

        Department department =
                DepartmentFactory.createDepartment(
                        "D001",
                        "Information Technology",
                        "Block A",
                        "Mr Adams"
                );

        assertNotNull(department);

        assertEquals(
                "Information Technology",
                department.getName()
        );
    }
}