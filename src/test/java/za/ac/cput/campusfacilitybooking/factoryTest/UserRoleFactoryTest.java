package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;
import za.ac.cput.campusfacilitybooking.factory.UserRoleFactory;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleFactoryTest {

    @Test
    void testCreateUserRole() {
        UserRole userRole = UserRoleFactory.createUserRole(
                "UR001",
                "U001",
                Role.STUDENT
        );

        assertNotNull(userRole);
        assertEquals("UR001", userRole.getUserRoleId());
        assertEquals("U001", userRole.getUserId());
        assertEquals(Role.STUDENT, userRole.getRole());
    }

    @Test
    void testCreateUserRoleWithInvalidUserRoleId() {
        assertThrows(IllegalArgumentException.class, () ->
                UserRoleFactory.createUserRole(
                        "",
                        "U001",
                        Role.STUDENT
                )
        );
    }

    @Test
    void testCreateUserRoleWithInvalidUserId() {
        assertThrows(IllegalArgumentException.class, () ->
                UserRoleFactory.createUserRole(
                        "UR002",
                        "",
                        Role.STUDENT
                )
        );
    }

    @Test
    void testCreateUserRoleWithNullRole() {
        assertThrows(IllegalArgumentException.class, () ->
                UserRoleFactory.createUserRole(
                        "UR003",
                        "U001",
                        null
                )
        );
    }
}