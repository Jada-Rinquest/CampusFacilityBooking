package za.ac.cput.campusfacilitybooking.serviceTest;

/* StaffServiceImplTest.java
   StaffServiceImpl Test class
   Author: Milani Sani (230371574)
   Date: 12 July 2026
*/

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;
import za.ac.cput.campusfacilitybooking.factory.StaffFactory;
import za.ac.cput.campusfacilitybooking.service.IStaffService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StaffServiceImplTest {

    @Autowired
    private IStaffService staffService;

    private Staff staff;

    @BeforeEach
    void setUp() {
        staff = StaffFactory.createStaff(
                "S001",
                "Thandi",
                "Khumalo",
                "thandi.khumalo@cput.ac.za",
                StaffRole.LECTURER,
                null);
    }

    @Test
    @Order(1)
    void create() {
        Staff created = staffService.create(staff);
        assertNotNull(created);
        assertEquals("S001", created.getStaffId());
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void read() {
        staffService.create(staff);
        Staff found = staffService.read("S001");
        assertNotNull(found);
        assertEquals("Thandi", found.getFirstName());
        System.out.println("Read: " + found);
    }

    @Test
    @Order(3)
    void update() {
        staffService.create(staff);
        Staff updated = new Staff.Builder()
                .staffId("S001")
                .firstName("Thandi")
                .lastName("Khumalo")
                .email("thandi.updated@cput.ac.za")
                .role(StaffRole.ADMIN)
                .department(null)
                .build();
        Staff result = staffService.update(updated);
        assertNotNull(result);
        assertEquals(StaffRole.ADMIN, result.getRole());
        assertEquals("thandi.updated@cput.ac.za", result.getEmail());
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void findByFirstName() {
        staffService.create(staff);
        List<Staff> results = staffService.findByFirstName("Thandi");
        assertFalse(results.isEmpty());
        System.out.println("Found by first name: " + results);
    }

    @Test
    @Order(5)
    void findByRole() {
        staffService.create(staff);
        List<Staff> results = staffService.findByRole(StaffRole.LECTURER);
        assertFalse(results.isEmpty());
        System.out.println("Found by role: " + results);
    }

    @Test
    @Order(6)
    void findByEmail() {
        staffService.create(staff);
        Staff found = staffService.findByEmail("thandi.khumalo@cput.ac.za");
        assertNotNull(found);
        System.out.println("Found by email: " + found);
    }

    @Test
    @Order(7)
    void getAll() {
        staffService.create(staff);
        Set<Staff> all = staffService.getAll();
        assertFalse(all.isEmpty());
        System.out.println("All staff: " + all);
    }

    @Test
    @Order(8)
    void delete() {
        staffService.create(staff);
        boolean deleted = staffService.delete("S001");
        assertTrue(deleted);
        assertNull(staffService.read("S001"));
        System.out.println("Successfully deleted staff S001");
    }

    @Test
    @Order(9)
    void delete_nonExistentId_returnsFalse() {
        boolean result = staffService.delete("DOES_NOT_EXIST");
        assertFalse(result);
        System.out.println("Delete non-existent returned: " + result);
    }

    @Test
    @Order(10)
    void update_nonExistentStaff_returnsNull() {
        Staff ghost = StaffFactory.createStaff(
                "GHOST", "Ghost", "User", "ghost@cput.ac.za",
                StaffRole.ADMIN, null);
        Staff result = staffService.update(ghost);
        assertNull(result);
        System.out.println("Update non-existent returned: " + result);
    }
}
