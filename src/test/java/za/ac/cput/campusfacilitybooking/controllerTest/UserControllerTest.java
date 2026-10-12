package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.UserController;
import za.ac.cput.campusfacilitybooking.controller.UserRequest;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.factory.UserFactory;
import za.ac.cput.campusfacilitybooking.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserService service;
    private UserController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(UserService.class);
        controller = new UserController(service);
    }

    @Test
    void testCreate() {
        UserRequest request = new UserRequest();
        request.setUserId("U001");
        request.setFirstName("Jada");
        request.setLastName("Rinquest");
        request.setEmail("jada@cput.ac.za");
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setDepartmentId("D001");

        User user = UserFactory.createUser(
                request.getUserId(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getDateOfBirth(),
                request.getDepartmentId()
        );

        when(service.create(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("U001", response.getBody().getUserId());
        assertEquals("Jada", response.getBody().getFirstName());
    }

    @Test
    void testRead() {
        User user = UserFactory.createUser(
                "U001",
                "Jada",
                "Rinquest",
                "jada@cput.ac.za",
                LocalDate.of(2000, 1, 1),
                "D001"
        );

        when(service.read("U001")).thenReturn(user);

        ResponseEntity<User> response = controller.read("U001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("U001", response.getBody().getUserId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("U999")).thenReturn(null);

        ResponseEntity<User> response = controller.read("U999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        User user = UserFactory.createUser(
                "U001",
                "Jada",
                "Smith",
                "jada.smith@cput.ac.za",
                LocalDate.of(2000, 1, 1),
                "D002"
        );

        when(service.update(user)).thenReturn(user);

        ResponseEntity<User> response = controller.update(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Smith", response.getBody().getLastName());
    }

    @Test
    void testDelete() {
        when(service.delete("U001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("U001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("U001");
    }
}