package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Contact;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.factory.ContactFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContactFactoryTest {

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
    void testCreateContact() {

        User user = createUser();

        Contact contact = ContactFactory.createContact(
                "C001",
                "0821234567",
                "Mobile Number",
                user
        );

        assertNotNull(contact);
        assertEquals("C001", contact.getContactId());
        assertEquals("0821234567", contact.getContact());
        assertEquals("Mobile Number", contact.getDescription());
        assertEquals(user, contact.getUser());
    }

    @Test
    void testCreateContactWithInvalidContactId() {

        assertThrows(IllegalArgumentException.class, () ->
                ContactFactory.createContact(
                        "",
                        "0821234567",
                        "Mobile Number",
                        createUser()
                )
        );
    }

    @Test
    void testCreateContactWithInvalidContact() {

        assertThrows(IllegalArgumentException.class, () ->
                ContactFactory.createContact(
                        "C002",
                        "",
                        "Mobile Number",
                        createUser()
                )
        );
    }

    @Test
    void testCreateContactWithInvalidDescription() {

        assertThrows(IllegalArgumentException.class, () ->
                ContactFactory.createContact(
                        "C003",
                        "0821234567",
                        "",
                        createUser()
                )
        );
    }

    @Test
    void testCreateContactWithNullUser() {

        assertThrows(IllegalArgumentException.class, () ->
                ContactFactory.createContact(
                        "C004",
                        "0821234567",
                        "Mobile Number",
                        null
                )
        );
    }
}
