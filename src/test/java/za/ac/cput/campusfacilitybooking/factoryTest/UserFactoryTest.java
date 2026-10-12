package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.factory.UserFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserFactoryTest {

    @Test
    void testCreateUser() {

        User user = UserFactory.createUser(
                "U001",
                "Jada",
                "Rinquest",
                "jada@cput.ac.za",
                LocalDate.of(2000, 1, 1),
                "D001"
        );

        assertNotNull(user);
        assertEquals("U001", user.getUserId());
        assertEquals("Jada", user.getFirstName());
        assertEquals("Rinquest", user.getLastName());
        assertEquals("jada@cput.ac.za", user.getEmail());
        assertEquals(
                LocalDate.of(2000, 1, 1),
                user.getDateOfBirth()
        );
        assertEquals("D001", user.getDepartmentId());
    }

    @Test
    void testCreateUserWithInvalidUserId() {

        assertThrows(IllegalArgumentException.class, () ->
                UserFactory.createUser(
                        "",
                        "Jada",
                        "Rinquest",
                        "jada@cput.ac.za",
                        LocalDate.of(2000, 1, 1),
                        "D001"
                )
        );
    }

    @Test
    void testCreateUserWithInvalidFirstName() {

        assertThrows(IllegalArgumentException.class, () ->
                UserFactory.createUser(
                        "U002",
                        "",
                        "Rinquest",
                        "jada@cput.ac.za",
                        LocalDate.of(2000, 1, 1),
                        "D001"
                )
        );
    }

    @Test
    void testCreateUserWithInvalidLastName() {

        assertThrows(IllegalArgumentException.class, () ->
                UserFactory.createUser(
                        "U003",
                        "Jada",
                        "",
                        "jada@cput.ac.za",
                        LocalDate.of(2000, 1, 1),
                        "D001"
                )
        );
    }

    @Test
    void testCreateUserWithInvalidEmail() {

        assertThrows(IllegalArgumentException.class, () ->
                UserFactory.createUser(
                        "U004",
                        "Jada",
                        "Rinquest",
                        "",
                        LocalDate.of(2000, 1, 1),
                        "D001"
                )
        );
    }

    @Test
    void testCreateUserWithNullDateOfBirth() {

        assertThrows(IllegalArgumentException.class, () ->
                UserFactory.createUser(
                        "U005",
                        "Jada",
                        "Rinquest",
                        "jada@cput.ac.za",
                        null,
                        "D001"
                )
        );
    }

    @Test
    void testCreateUserWithInvalidDepartmentId() {

        assertThrows(IllegalArgumentException.class, () ->
                UserFactory.createUser(
                        "U006",
                        "Jada",
                        "Rinquest",
                        "jada@cput.ac.za",
                        LocalDate.of(2000, 1, 1),
                        ""
                )
        );
    }
}
