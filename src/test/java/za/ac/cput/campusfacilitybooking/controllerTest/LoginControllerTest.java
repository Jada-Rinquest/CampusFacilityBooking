package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.LoginController;
import za.ac.cput.campusfacilitybooking.controller.LoginRequest;
import za.ac.cput.campusfacilitybooking.domain.Login;
import za.ac.cput.campusfacilitybooking.factory.LoginFactory;
import za.ac.cput.campusfacilitybooking.service.LoginService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    private LoginService service;
    private LoginController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(LoginService.class);
        controller = new LoginController(service);
    }

    @Test
    void testCreate() {
        LoginRequest request = new LoginRequest();
        request.setLoginId("L001");
        request.setRegistrarId("R001");
        request.setUsername("admin");
        request.setPassword("password123");

        Login login = LoginFactory.createLogin(
                request.getLoginId(),
                request.getRegistrarId(),
                request.getUsername(),
                request.getPassword()
        );

        when(service.create(any(Login.class))).thenReturn(login);

        ResponseEntity<Login> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("L001", response.getBody().getLoginId());
        assertEquals("admin", response.getBody().getUsername());
    }

    @Test
    void testRead() {
        Login login = LoginFactory.createLogin(
                "L001",
                "R001",
                "admin",
                "password123"
        );

        when(service.read("L001")).thenReturn(login);

        ResponseEntity<Login> response = controller.read("L001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("L001", response.getBody().getLoginId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("L999")).thenReturn(null);

        ResponseEntity<Login> response = controller.read("L999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        Login login = LoginFactory.createLogin(
                "L001",
                "R001",
                "admin_updated",
                "newpassword123"
        );

        when(service.update(login)).thenReturn(login);

        ResponseEntity<Login> response = controller.update(login);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("admin_updated", response.getBody().getUsername());
    }

    @Test
    void testDelete() {
        when(service.delete("L001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("L001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("L001");
    }
}