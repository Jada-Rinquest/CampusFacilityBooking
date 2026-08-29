package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Login;
import za.ac.cput.campusfacilitybooking.factory.LoginFactory;

import static org.junit.jupiter.api.Assertions.*;

class LoginFactoryTest {

    @Test
    void testCreateLogin() {
        Login login = LoginFactory.createLogin(
                "L001",
                "R001",
                "admin",
                "password123"
        );

        assertNotNull(login);
        assertEquals("L001", login.getLoginId());
        assertEquals("R001", login.getRegistrarId());
        assertEquals("admin", login.getUsername());
        assertEquals("password123", login.getPassword());
    }

    @Test
    void testCreateLoginWithInvalidLoginId() {
        assertThrows(IllegalArgumentException.class, () ->
                LoginFactory.createLogin(
                        "",
                        "R001",
                        "admin",
                        "password123"
                )
        );
    }

    @Test
    void testCreateLoginWithInvalidRegistrarId() {
        assertThrows(IllegalArgumentException.class, () ->
                LoginFactory.createLogin(
                        "L002",
                        "",
                        "admin",
                        "password123"
                )
        );
    }

    @Test
    void testCreateLoginWithInvalidUsername() {
        assertThrows(IllegalArgumentException.class, () ->
                LoginFactory.createLogin(
                        "L003",
                        "R001",
                        "",
                        "password123"
                )
        );
    }

    @Test
    void testCreateLoginWithInvalidPassword() {
        assertThrows(IllegalArgumentException.class, () ->
                LoginFactory.createLogin(
                        "L004",
                        "R001",
                        "admin",
                        ""
                )
        );
    }
}