package za.ac.cput.campusfacilitybooking.factoryTest;

/* Author: Milani Sani(230371574)
   Date: 28 June 2026 */

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.factory.StaffFactory;

import static org.junit.jupiter.api.Assertions.*;

class StaffFactoryTest {

    @Test
    void testCreateStaff() {

        User user = null;

        Staff staff = StaffFactory.createStaff(
                "S001",
                user
        );

        assertNotNull(staff);
        assertEquals("S001", staff.getStaffId());
        assertNull(staff.getUser());
    }

    @Test
    void testCreateStaffWithInvalidStaffId() {

        assertThrows(IllegalArgumentException.class, () ->
                StaffFactory.createStaff(
                        "",
                        null
                )
        );
    }

    @Test
    void testCreateStaffWithNullStaffId() {

        assertThrows(IllegalArgumentException.class, () ->
                StaffFactory.createStaff(
                        null,
                        null
                )
        );
    }

    @Test
    void testCreateStaffWithNullUser() {

        assertThrows(IllegalArgumentException.class, () ->
                StaffFactory.createStaff(
                        "S002",
                        null
                )
        );
    }
}