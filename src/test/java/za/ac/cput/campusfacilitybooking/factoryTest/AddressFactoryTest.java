package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Address;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.factory.AddressFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AddressFactoryTest {

    private User createUser() {
        return new User(
                "U001",
                "Jada",
                "Rinquest",
                "jada@cput.ac.za",
                LocalDate.of(2000, 1, 1),
                "D001"
        );
    }

    @Test
    void testCreateAddress() {

        User user = createUser();

        Address address = AddressFactory.createAddress(
                "A001",
                "Cape Town, Western Cape",
                "Home Address",
                user
        );

        assertNotNull(address);
        assertEquals("A001", address.getAddressId());
        assertEquals("Cape Town, Western Cape", address.getAddress());
        assertEquals("Home Address", address.getDescription());
        assertEquals(user, address.getUser());
    }

    @Test
    void testCreateAddressWithInvalidAddressId() {

        assertThrows(IllegalArgumentException.class, () ->
                AddressFactory.createAddress(
                        "",
                        "Cape Town, Western Cape",
                        "Home Address",
                        createUser()
                )
        );
    }

    @Test
    void testCreateAddressWithInvalidAddress() {

        assertThrows(IllegalArgumentException.class, () ->
                AddressFactory.createAddress(
                        "A002",
                        "",
                        "Home Address",
                        createUser()
                )
        );
    }

    @Test
    void testCreateAddressWithInvalidDescription() {

        assertThrows(IllegalArgumentException.class, () ->
                AddressFactory.createAddress(
                        "A003",
                        "Cape Town, Western Cape",
                        "",
                        createUser()
                )
        );
    }

    @Test
    void testCreateAddressWithNullUser() {

        assertThrows(IllegalArgumentException.class, () ->
                AddressFactory.createAddress(
                        "A004",
                        "Cape Town, Western Cape",
                        "Home Address",
                        null
                )
        );
    }
}
