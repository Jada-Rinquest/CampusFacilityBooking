package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;
import za.ac.cput.campusfacilitybooking.factory.UserRoleFactory;
import za.ac.cput.campusfacilitybooking.repository.UserRoleRepository;
import za.ac.cput.campusfacilitybooking.service.impl.UserRoleServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserRoleServiceTest {

    private UserRoleRepository repository;
    private UserRoleServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRoleRepository.class);
        service = new UserRoleServiceImpl(repository);
    }

    @Test
    void testCreate() {
        UserRole userRole = UserRoleFactory.createUserRole(
                "UR001",
                "U001",
                Role.STUDENT
        );

        when(repository.save(userRole)).thenReturn(userRole);

        UserRole created = service.create(userRole);

        assertNotNull(created);
        assertEquals(userRole.getUserRoleId(), created.getUserRoleId());
    }

    @Test
    void testRead() {
        UserRole userRole = UserRoleFactory.createUserRole(
                "UR001",
                "U001",
                Role.STUDENT
        );

        when(repository.findById(userRole.getUserRoleId()))
                .thenReturn(Optional.of(userRole));

        UserRole found = service.read(userRole.getUserRoleId());

        assertNotNull(found);
        assertEquals(userRole.getUserRoleId(), found.getUserRoleId());
    }

    @Test
    void testUpdate() {
        UserRole userRole = UserRoleFactory.createUserRole(
                "UR001",
                "U001",
                Role.LECTURER
        );

        when(repository.save(userRole)).thenReturn(userRole);

        UserRole updated = service.update(userRole);

        assertNotNull(updated);
        assertEquals(Role.LECTURER, updated.getRole());
    }

    @Test
    void testDelete() {
        String id = "UR001";

        when(repository.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        verify(repository).deleteById(id);
        assertTrue(deleted);
    }
}