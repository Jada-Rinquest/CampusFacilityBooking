package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.AddressController;
import za.ac.cput.campusfacilitybooking.controller.AddressRequest;
import za.ac.cput.campusfacilitybooking.domain.Address;
import za.ac.cput.campusfacilitybooking.factory.AddressFactory;
import za.ac.cput.campusfacilitybooking.service.AddressService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddressControllerTest {

    private AddressService service;
    private AddressController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(AddressService.class);
        controller = new AddressController(service);
    }

    @Test
    void testCreate() {
        AddressRequest request = new AddressRequest();
        request.setAddressId("A001");
        request.setAddress("123 Main St");
        request.setDescription("Home Address");
        request.setUserId("U001");

        Address address = AddressFactory.createAddress(
                request.getAddressId(),
                request.getAddress(),
                request.getDescription(),
                request.getUserId()
        );

        when(service.create(any(Address.class))).thenReturn(address);

        ResponseEntity<Address> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("A001", response.getBody().getAddressId());
        assertEquals("U001", response.getBody().getUserId());
    }

    @Test
    void testRead() {
        Address address = AddressFactory.createAddress(
                "A001",
                "123 Main St",
                "Home Address",
                "U001"
        );

        when(service.read("A001")).thenReturn(address);

        ResponseEntity<Address> response = controller.read("A001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("A001", response.getBody().getAddressId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("A999")).thenReturn(null);

        ResponseEntity<Address> response = controller.read("A999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Address address = AddressFactory.createAddress(
                "A001",
                "456 New St",
                "Work Address",
                "U001"
        );

        when(service.update(address)).thenReturn(address);

        ResponseEntity<Address> response = controller.update(address);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("456 New St", response.getBody().getAddress());
    }

    @Test
    void testDelete() {
        when(service.delete("A001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("A001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("A001");
    }
}