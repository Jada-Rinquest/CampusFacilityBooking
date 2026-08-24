package za.ac.cput.campusfacilitybooking.factoryTest;

/*Author: Milani Sani(230371574)
Date: 28 June 2026
 */

import za.ac.cput.campusfacilitybooking.domain.Department;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;
import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.factory.StaffFactory;

import static org.junit.jupiter.api.Assertions.*;

class StaffFactoryTest {

    private Department buildDepartment() {
        return new Department.Builder()
                .setDepartmentId("D001")
                .setName("Information Technology")
                .build();
    }

    @Test
    void createStaff_validData_returnsStaff() {
        Department department = buildDepartment();

        Staff staff = StaffFactory.createStaff(
                "S001", "Thandiwe", "Khumalo", "thandiwe@cput.ac.za",
                StaffRole.LECTURER, department);

        assertNotNull(staff);
        assertEquals("S001", staff.getStaffId());
        assertEquals("Thandiwe", staff.getFirstName());
        assertEquals("Khumalo", staff.getLastName());
        assertEquals(StaffRole.LECTURER, staff.getRole());
        assertEquals(department, staff.getDepartment());
    }

    @Test
    void createStaff_nullStaffId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                StaffFactory.createStaff(
                        null, "Thandiwe", "Khumalo", "thandiwe@cput.ac.za",
                        StaffRole.LECTURER, buildDepartment()));
    }

    @Test
    void createStaff_missingLastName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                StaffFactory.createStaff(
                        "S003", "Sipho", null, "sipho@cout.ac.za",
                        StaffRole.STAFF, buildDepartment()));
    }

    @Test
    void createStaff_missingRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                StaffFactory.createStaff(
                        "S004", "Lerato", "Moeketsi", "lerato@cput.ac.za",
                        null, buildDepartment()));
    }

    @Test
    void createStaff_nullDepartment_stillCreatesStaff() {
        Staff staff = StaffFactory.createStaff(
                "S005", "Naledi", "Mokoena", "naledi@cput.ac.za",
                StaffRole.LECTURER, null);

        assertNotNull(staff);
        assertNull(staff.getDepartment());
    }
}