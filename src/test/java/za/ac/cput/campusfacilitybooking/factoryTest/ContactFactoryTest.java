package za.ac.cput.campusfacilitybooking.factoryTest;

import org.junit.jupiter.api.Test;
import za.ac.cput.campusfacilitybooking.domain.Contact;
import za.ac.cput.campusfacilitybooking.factory.ContactFactory;

import static org.junit.jupiter.api.Assertions.*;

class ContactFactoryTest {

    @Test
    void testCreateContact() {
        Contact contact = ContactFactory.createContact(
                "C001",
                "0821234567",
                "Mobile Number",
                "U001"
        );

        assertNotNull(contact);
        assertEquals("C001", contact.getContactId());
        assertEquals("0821234567", contact.getContact());
        assertEquals("Mobile Number", contact.getDescription());
        assertEquals("U001", contact.getUserId());
    }

    @Test
    void testCreateContactWithInvalidContactId() {
        assertThrows(IllegalArgumentException.class, () ->
                ContactFactory.createContact(
                        "",
                        "0821234567",
                        "Mobile Number",
                        "U001"
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
                        "U001"
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
                        "U001"
                )
        );
    }

    @Test
    void testCreateContactWithNullUserId() {
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