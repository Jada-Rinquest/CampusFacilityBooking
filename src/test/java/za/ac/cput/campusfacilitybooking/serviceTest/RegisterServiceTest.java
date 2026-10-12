package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.factory.RegisterFactory;
import za.ac.cput.campusfacilitybooking.repository.RegisterRepository;
import za.ac.cput.campusfacilitybooking.service.impl.RegisterServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterServiceTest {

    private RegisterRepository repository;
    private RegisterServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(RegisterRepository.class);
        service = new RegisterServiceImpl(repository);
    }

    @Test
    void testCreate() {
        Register register = RegisterFactory.createRegister(
                "R001",
                "registrar@cput.ac.za",
                LocalDate.of(2026, 8, 24)
        );

        when(repository.save(register)).thenReturn(register);

        Register created = service.create(register);

        assertNotNull(created);
        assertEquals(register.getRegistrarId(), created.getRegistrarId());
    }

    @Test
    void testRead() {
        Register register = RegisterFactory.createRegister(
                "R001",
                "registrar@cput.ac.za",
                LocalDate.of(2026, 8, 24)
        );

        when(repository.findById(register.getRegistrarId()))
                .thenReturn(Optional.of(register));

        Register found = service.read(register.getRegistrarId());

        assertNotNull(found);
        assertEquals(register.getRegistrarId(), found.getRegistrarId());
    }

    @Test
    void testUpdate() {
        Register register = RegisterFactory.createRegister(
                "R001",
                "newregistrar@cput.ac.za",
                LocalDate.of(2026, 8, 25)
        );

        when(repository.save(register)).thenReturn(register);

        Register updated = service.update(register);

        assertNotNull(updated);
        assertEquals("newregistrar@cput.ac.za", updated.getEmail());
    }

    @Test
    void testDelete() {
        String id = "R001";

        when(repository.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        verify(repository).deleteById(id);
        assertTrue(deleted);
    }
}