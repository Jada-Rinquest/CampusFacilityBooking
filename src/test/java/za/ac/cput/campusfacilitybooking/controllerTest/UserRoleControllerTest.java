package za.ac.cput.campusfacilitybooking.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.campusfacilitybooking.controller.UserRoleController;
import za.ac.cput.campusfacilitybooking.controller.UserRoleRequest;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;
import za.ac.cput.campusfacilitybooking.factory.UserRoleFactory;
import za.ac.cput.campusfacilitybooking.service.UserRoleService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserRoleControllerTest {

    private UserRoleService service;
    private UserRoleController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(UserRoleService.class);
        controller = new UserRoleController(service);
    }

    @Test
    void testCreate() {
        UserRoleRequest request = new UserRoleRequest();
        request.setUserRoleId("UR001");
        request.setUserId("U001");
        request.setRole(Role.STUDENT);

        UserRole userRole = UserRoleFactory.createUserRole(
                request.getUserRoleId(),
                request.getUserId(),
                request.getRole()
        );

        when(service.create(any(UserRole.class))).thenReturn(userRole);

        ResponseEntity<UserRole> response = controller.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UR001", response.getBody().getUserRoleId());
        assertEquals(Role.STUDENT, response.getBody().getRole());
    }

    @Test
    void testRead() {
        UserRole userRole = UserRoleFactory.createUserRole(
                "UR001",
                "U001",
                Role.STUDENT
        );

        when(service.read("UR001")).thenReturn(userRole);

        ResponseEntity<UserRole> response = controller.read("UR001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UR001", response.getBody().getUserRoleId());
    }

    @Test
    void testReadNotFound() {
        when(service.read("UR999")).thenReturn(null);

        ResponseEntity<UserRole> response = controller.read("UR999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate() {
        UserRole userRole = UserRoleFactory.createUserRole(
                "UR001",
                "U001",
                Role.LECTURER
        );

        when(service.update(userRole)).thenReturn(userRole);

        ResponseEntity<UserRole> response = controller.update(userRole);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Role.LECTURER, response.getBody().getRole());
    }

    @Test
    void testDelete() {
        when(service.delete("UR001")).thenReturn(true);

        ResponseEntity<Boolean> response = controller.delete("UR001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(service).delete("UR001");
    }
}