// Jada Rinquest 12/07/2026
// 222871296

package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;

import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.factory.DepartmentFactory;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentFactoryTest {

    @Test
    void testCreateDepartment() {

        Department department = DepartmentFactory.createDepartment(
                "D001",
                "Information Technology",
                "Block A",
                "Mr Adams"
        );

        assertNotNull(department);
        assertEquals("D001", department.getDepartmentId());
        assertEquals("Information Technology", department.getName());
        assertEquals("Block A", department.getBuilding());
        assertEquals("Mr Adams", department.getHeadOfDepartment());
    }

    @Test
    void testCreateDepartmentWithInvalidDepartmentId() {

        assertThrows(IllegalArgumentException.class, () ->
                DepartmentFactory.createDepartment(
                        "",
                        "Information Technology",
                        "Block A",
                        "Mr Adams"
                )
        );
    }
}