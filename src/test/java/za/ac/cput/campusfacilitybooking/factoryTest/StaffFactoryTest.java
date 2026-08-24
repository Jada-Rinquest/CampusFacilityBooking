package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Milani Sani(230371574)
   Date: 28 June 2026 */

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.factory.StaffFactory;

import static org.junit.jupiter.api.Assertions.*;

class StaffFactoryTest {

    @Test
    void testCreateStaff() {
        Staff staff = StaffFactory.createStaff(
                "S001",
                "U001"
        );

        assertNotNull(staff);
        assertEquals("S001", staff.getStaffId());
        assertEquals("U001", staff.getUserId());
    }

    @Test
    void testCreateStaffWithInvalidStaffId() {
        assertThrows(IllegalArgumentException.class, () ->
                StaffFactory.createStaff(
                        "",
                        "U001"
                )
        );
    }

    @Test
    void testCreateStaffWithNullStaffId() {
        assertThrows(IllegalArgumentException.class, () ->
                StaffFactory.createStaff(
                        null,
                        "U001"
                )
        );
    }

    @Test
    void testCreateStaffWithNullUserId() {
        assertThrows(IllegalArgumentException.class, () ->
                StaffFactory.createStaff(
                        "S002",
                        null
                )
        );
    }

    @Test
    void testCreateStaffWithEmptyUserId() {
        assertThrows(IllegalArgumentException.class, () ->
                StaffFactory.createStaff(
                        "S003",
                        ""
                )
        );
    }
}