package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.ContactController;
import za.ac.cput.campusfacilitybooking.controller.ContactRequest;
import za.ac.cput.campusfacilitybooking.domain.Contact;
import za.ac.cput.campusfacilitybooking.factory.ContactFactory;
import za.ac.cput.campusfacilitybooking.service.ContactService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContactControllerTest {

    private ContactService service;
    private ContactController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(ContactService.class);
        controller = new ContactController(service);
    }

    @Test
    void testCreate() {
        ContactRequest request = new ContactRequest();
        request.setContactId("C001");
        request.setContact("0821234567");
        request.setDescription("Mobile Number");
        request.setUserId("U001");

        Contact contact = ContactFactory.createContact(
                request.getContactId(),
                request.getContact(),
                request.getDescription(),
                request.getUserId()
        );

        when(service.create(any(Contact.class))).thenReturn(contact);

        ResponseEntity<Contact> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("C001", response.getBody().getContactId());
        assertEquals("U001", response.getBody().getUserId());
    }

    @Test
    void testRead() {
        Contact contact = ContactFactory.createContact(
                "C001",
                "0821234567",
                "Mobile Number",
                "U001"
        );

        when(service.read("C001")).thenReturn(contact);

        ResponseEntity<Contact> response = controller.read("C001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("C001", response.getBody().getContactId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("C999")).thenReturn(null);

        ResponseEntity<Contact> response = controller.read("C999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Contact contact = ContactFactory.createContact(
                "C001",
                "0829876543",
                "Work Number",
                "U001"
        );

        when(service.update(contact)).thenReturn(contact);

        ResponseEntity<Contact> response = controller.update(contact);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("0829876543", response.getBody().getContact());
    }

    @Test
    void testDelete() {
        when(service.delete("C001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("C001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("C001");
    }
}