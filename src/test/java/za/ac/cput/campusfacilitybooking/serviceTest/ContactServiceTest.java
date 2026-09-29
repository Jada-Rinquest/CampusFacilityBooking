package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Contact;
import za.ac.cput.campusfacilitybooking.factory.ContactFactory;
import za.ac.cput.campusfacilitybooking.repository.ContactRepository;
import za.ac.cput.campusfacilitybooking.service.impl.ContactServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContactServiceTest {

    private ContactRepository repository;
    private ContactServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ContactRepository.class);
        service = new ContactServiceImpl(repository);
    }

    @Test
    void testCreate() {
        Contact contact = ContactFactory.createContact(
                "C001",
                "0821234567",
                "Mobile Number",
                "U001"
        );

        when(repository.save(contact)).thenReturn(contact);

        Contact created = service.create(contact);

        assertNotNull(created);
        assertEquals(contact.getContactId(), created.getContactId());
    }

    @Test
    void testRead() {
        Contact contact = ContactFactory.createContact(
                "C001",
                "0821234567",
                "Mobile Number",
                "U001"
        );

        when(repository.findById(contact.getContactId()))
                .thenReturn(Optional.of(contact));

        Contact found = service.read(contact.getContactId());

        assertNotNull(found);
        assertEquals(contact.getContactId(), found.getContactId());
    }

    @Test
    void testUpdate() {
        Contact contact = ContactFactory.createContact(
                "C001",
                "0829876543",
                "Work Number",
                "U001"
        );

        when(repository.save(contact)).thenReturn(contact);

        Contact updated = service.update(contact);

        assertNotNull(updated);
        assertEquals("0829876543", updated.getContact());
    }

    @Test
    void testDelete() {
        String id = "C001";

        when(repository.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        verify(repository).deleteById(id);
        assertTrue(deleted);
    }
}