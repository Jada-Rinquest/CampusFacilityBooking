package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.factory.UserRoleFactory;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleFactoryTest {

    @Test
    void testCreateUserRole() {

        UserRole userRole = UserRoleFactory.createUserRole(
                "UR001",
                "U001",
                "R001"
        );

        assertNotNull(userRole);
        assertEquals("UR001", userRole.getUserRoleId());
        assertEquals("U001", userRole.getUserId());
        assertEquals("R001", userRole.getRoleId());
    }

    @Test
    void testCreateUserRoleWithInvalidUserRoleId() {

        assertThrows(IllegalArgumentException.class, () ->
                UserRoleFactory.createUserRole(
                        "",
                        "U001",
                        "R001"
                )
        );
    }

    @Test
    void testCreateUserRoleWithInvalidUserId() {

        assertThrows(IllegalArgumentException.class, () ->
                UserRoleFactory.createUserRole(
                        "UR002",
                        "",
                        "R001"
                )
        );
    }

    @Test
    void testCreateUserRoleWithInvalidRoleId() {

        assertThrows(IllegalArgumentException.class, () ->
                UserRoleFactory.createUserRole(
                        "UR003",
                        "U001",
                        ""
                )
        );
    }
}
