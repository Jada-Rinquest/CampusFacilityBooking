package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Login;
import za.ac.cput.campusfacilitybooking.factory.LoginFactory;
import za.ac.cput.campusfacilitybooking.repository.LoginRepository;
import za.ac.cput.campusfacilitybooking.service.impl.LoginServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    private LoginRepository repository;
    private LoginServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(LoginRepository.class);
        service = new LoginServiceImpl(repository);
    }

    @Test
    void testCreate() {
        Login login = LoginFactory.createLogin(
                "L001",
                "R001",
                "admin",
                "password123"
        );

        when(repository.save(login)).thenReturn(login);

        Login created = service.create(login);

        assertNotNull(created);
        assertEquals(login.getLoginId(), created.getLoginId());
    }

    @Test
    void testRead() {
        Login login = LoginFactory.createLogin(
                "L001",
                "R001",
                "admin",
                "password123"
        );

        when(repository.findById(login.getLoginId()))
                .thenReturn(Optional.of(login));

        Login found = service.read(login.getLoginId());

        assertNotNull(found);
        assertEquals(login.getLoginId(), found.getLoginId());
    }

    @Test
    void testUpdate() {
        Login login = LoginFactory.createLogin(
                "L001",
                "R001",
                "admin_updated",
                "newpassword123"
        );

        when(repository.save(login)).thenReturn(login);

        Login updated = service.update(login);

        assertNotNull(updated);
        assertEquals("admin_updated", updated.getUsername());
    }

    @Test
    void testDelete() {
        String id = "L001";

        when(repository.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        verify(repository).deleteById(id);
        assertTrue(deleted);
    }
}