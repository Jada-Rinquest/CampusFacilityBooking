package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.RegisterController;
import za.ac.cput.campusfacilitybooking.controller.RegisterRequest;
import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.factory.RegisterFactory;
import za.ac.cput.campusfacilitybooking.service.RegisterService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterControllerTest {

    private RegisterService service;
    private RegisterController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(RegisterService.class);
        controller = new RegisterController(service);
    }

    @Test
    void testCreate() {
        RegisterRequest request = new RegisterRequest();
        request.setRegistrarId("R001");
        request.setEmail("registrar@cput.ac.za");
        request.setDateRegistered(LocalDate.of(2026, 8, 24));

        Register register = RegisterFactory.createRegister(
                request.getRegistrarId(),
                request.getEmail(),
                request.getDateRegistered()
        );

        when(service.create(any(Register.class))).thenReturn(register);

        ResponseEntity<Register> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("R001", response.getBody().getRegistrarId());
        assertEquals("registrar@cput.ac.za", response.getBody().getEmail());
    }

    @Test
    void testRead() {
        Register register = RegisterFactory.createRegister(
                "R001",
                "registrar@cput.ac.za",
                LocalDate.of(2026, 8, 24)
        );

        when(service.read("R001")).thenReturn(register);

        ResponseEntity<Register> response = controller.read("R001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("R001", response.getBody().getRegistrarId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("R999")).thenReturn(null);

        ResponseEntity<Register> response = controller.read("R999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Register register = RegisterFactory.createRegister(
                "R001",
                "newregistrar@cput.ac.za",
                LocalDate.of(2026, 8, 25)
        );

        when(service.update(register)).thenReturn(register);

        ResponseEntity<Register> response = controller.update(register);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newregistrar@cput.ac.za", response.getBody().getEmail());
    }

    @Test
    void testDelete() {
        when(service.delete("R001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("R001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("R001");
    }
}