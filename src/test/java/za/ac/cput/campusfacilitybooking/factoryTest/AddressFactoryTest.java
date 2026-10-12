package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Address;
import za.ac.cput.campusfacilitybooking.factory.AddressFactory;

import static org.junit.jupiter.api.Assertions.*;

class AddressFactoryTest {

    @Test
    void testCreateAddress() {
        Address address = AddressFactory.createAddress(
                "A001",
                "Cape Town, Western Cape",
                "Home Address",
                "U001"
        );

        assertNotNull(address);
        assertEquals("A001", address.getAddressId());
        assertEquals("Cape Town, Western Cape", address.getAddress());
        assertEquals("Home Address", address.getDescription());
        assertEquals("U001", address.getUserId());
    }

    @Test
    void testCreateAddressWithInvalidAddressId() {
        assertThrows(IllegalArgumentException.class, () ->
                AddressFactory.createAddress(
                        "",
                        "Cape Town, Western Cape",
                        "Home Address",
                        "U001"
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
                        "U001"
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
                        "U001"
                )
        );
    }

    @Test
    void testCreateAddressWithNullUserId() {
        assertThrows(IllegalArgumentException.class, () ->
                AddressFactory.createAddress(
                        "A004",
                        "Cape Town, Western Cape",
                        "Home Address",
                        null
                )
        );
    }

    @Test
    void testCreateAddressWithEmptyUserId() {
        assertThrows(IllegalArgumentException.class, () ->
                AddressFactory.createAddress(
                        "A005",
                        "Cape Town, Western Cape",
                        "Home Address",
                        ""
                )
        );
    }
}