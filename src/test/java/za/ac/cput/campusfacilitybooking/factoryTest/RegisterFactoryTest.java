package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.factory.RegisterFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RegisterFactoryTest {

    @Test
    void testCreateRegister() {

        Register register = RegisterFactory.createRegister(
                "R001",
                "registrar@cput.ac.za",
                LocalDate.of(2026, 8, 24)
        );

        assertNotNull(register);
        assertEquals("R001", register.getRegistrarId());
        assertEquals("registrar@cput.ac.za", register.getEmail());
        assertEquals(
                LocalDate.of(2026, 8, 24),
                register.getDateRegistered()
        );
    }

    @Test
    void testCreateRegisterWithInvalidRegistrarId() {

        assertThrows(IllegalArgumentException.class, () ->
                RegisterFactory.createRegister(
                        "",
                        "registrar@cput.ac.za",
                        LocalDate.of(2026, 8, 24)
                )
        );
    }

    @Test
    void testCreateRegisterWithInvalidEmail() {

        assertThrows(IllegalArgumentException.class, () ->
                RegisterFactory.createRegister(
                        "R002",
                        "",
                        LocalDate.of(2026, 8, 24)
                )
        );
    }

    @Test
    void testCreateRegisterWithNullDate() {

        assertThrows(IllegalArgumentException.class, () ->
                RegisterFactory.createRegister(
                        "R003",
                        "registrar@cput.ac.za",
                        null
                )
        );
    }
}
