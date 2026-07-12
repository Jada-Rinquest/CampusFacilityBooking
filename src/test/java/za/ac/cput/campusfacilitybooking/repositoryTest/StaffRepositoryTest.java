package za.ac.cput.campusfacilitybooking.repositoryTest;

/* StaffRepositoryTest.java
   StaffRepository Test class
   Author: Milani Sani (230371574)
   Date: 21 June 2026
*/

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.campusfacilitybooking.domain.Staff;
import za.ac.cput.campusfacilitybooking.domain.enums.StaffRole;
import za.ac.cput.campusfacilitybooking.factory.StaffFactory;
import za.ac.cput.campusfacilitybooking.repository.StaffRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;

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
    void save() {
        Staff saved = staffRepository.save(staff);
        assertNotNull(saved);
        assertEquals("S001", saved.getStaffId());
        System.out.println("Saved: " + saved);
    }

    @Test
    void findById() {
        staffRepository.save(staff);
        Optional<Staff> found = staffRepository.findById("S001");
        assertTrue(found.isPresent());
        assertEquals("Thandi", found.get().getFirstName());
        System.out.println("Found by ID: " + found.get());
    }

    @Test
    void findByFirstName() {
        staffRepository.save(staff);
        List<Staff> results = staffRepository.findByFirstName("Thandi");
        assertFalse(results.isEmpty());
        System.out.println("Found by first name: " + results);
    }

    @Test
    void findByRole() {
        staffRepository.save(staff);
        List<Staff> results = staffRepository.findByRole(StaffRole.LECTURER);
        assertFalse(results.isEmpty());
        System.out.println("Found by role: " + results);
    }

    @Test
    void findByEmail() {
        staffRepository.save(staff);
        Staff found = staffRepository.findByEmail("thandi.khumalo@cput.ac.za");
        assertNotNull(found);
        System.out.println("Found by email: " + found);
    }

    @Test
    void update() {
        staffRepository.save(staff);
        Staff updated = new Staff.Builder()
                .staffId("S001")
                .firstName("Thandi")
                .lastName("Khumalo")
                .email("updated.thandi@cput.ac.za")
                .role(StaffRole.ADMIN)
                .department(null)
                .build();
        Staff saved = staffRepository.save(updated);
        assertEquals(StaffRole.ADMIN, saved.getRole());
        System.out.println("Updated: " + saved);
    }

    @Test
    void delete() {
        staffRepository.save(staff);
        staffRepository.deleteById("S001");
        Optional<Staff> deleted = staffRepository.findById("S001");
        assertFalse(deleted.isPresent());
        System.out.println("Successfully deleted staff with ID S001");
    }

    @Test
    void findAll() {
        staffRepository.save(staff);
        List<Staff> all = staffRepository.findAll();
        assertFalse(all.isEmpty());
        System.out.println("All staff: " + all);
    }
}
