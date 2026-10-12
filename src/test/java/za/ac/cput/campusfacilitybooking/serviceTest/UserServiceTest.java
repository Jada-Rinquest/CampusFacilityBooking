package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.factory.UserFactory;
import za.ac.cput.campusfacilitybooking.repository.UserRepository;
import za.ac.cput.campusfacilitybooking.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository repository;
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        service = new UserServiceImpl(repository);
    }

    @Test
    void testCreate() {
        User user = UserFactory.createUser(
                "U001",
                "Jada",
                "Rinquest",
                "jada@cput.ac.za",
                LocalDate.of(2000, 1, 1),
                "D001"
        );

        when(repository.save(user)).thenReturn(user);

        User created = service.create(user);

        assertNotNull(created);
        assertEquals(user.getUserId(), created.getUserId());
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

        when(repository.findById(user.getUserId()))
                .thenReturn(Optional.of(user));

        User found = service.read(user.getUserId());

        assertNotNull(found);
        assertEquals(user.getUserId(), found.getUserId());
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

        when(repository.save(user)).thenReturn(user);

        User updated = service.update(user);

        assertNotNull(updated);
        assertEquals("Smith", updated.getLastName());
    }

    @Test
    void testDelete() {
        String id = "U001";

        when(repository.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        verify(repository).deleteById(id);
        assertTrue(deleted);
    }
}