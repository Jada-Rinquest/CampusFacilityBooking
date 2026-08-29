package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Address;
import za.ac.cput.campusfacilitybooking.factory.AddressFactory;
import za.ac.cput.campusfacilitybooking.repository.AddressRepository;
import za.ac.cput.campusfacilitybooking.service.impl.AddressServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

    private AddressRepository repository;
    private AddressServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(AddressRepository.class);
        service = new AddressServiceImpl(repository);
    }

    @Test
    void testCreate() {
        Address address = AddressFactory.createAddress(
                "A001",
                "123 Main St",
                "Home Address",
                "U001"
        );

        when(repository.save(address)).thenReturn(address);

        Address created = service.create(address);

        assertNotNull(created);
        assertEquals(address.getAddressId(), created.getAddressId());
    }

    @Test
    void testRead() {
        Address address = AddressFactory.createAddress(
                "A001",
                "123 Main St",
                "Home Address",
                "U001"
        );

        when(repository.findById(address.getAddressId()))
                .thenReturn(Optional.of(address));

        Address found = service.read(address.getAddressId());

        assertNotNull(found);
        assertEquals(address.getAddressId(), found.getAddressId());
    }

    @Test
    void testUpdate() {
        Address address = AddressFactory.createAddress(
                "A001",
                "456 New St",
                "Work Address",
                "U001"
        );

        when(repository.save(address)).thenReturn(address);

        Address updated = service.update(address);

        assertNotNull(updated);
        assertEquals("456 New St", updated.getAddress());
    }

    @Test
    void testDelete() {
        String id = "A001";

        when(repository.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        verify(repository).deleteById(id);
        assertTrue(deleted);
    }
}