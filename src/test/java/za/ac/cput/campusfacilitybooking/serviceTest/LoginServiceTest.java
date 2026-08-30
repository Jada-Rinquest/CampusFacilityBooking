package za.ac.cput.campusfacilitybooking.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import za.ac.cput.campusfacilitybooking.domain.Login;
import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;
import za.ac.cput.campusfacilitybooking.dto.AuthResponse;
import za.ac.cput.campusfacilitybooking.factory.LoginFactory;
import za.ac.cput.campusfacilitybooking.factory.RegisterFactory;
import za.ac.cput.campusfacilitybooking.factory.UserFactory;
import za.ac.cput.campusfacilitybooking.factory.UserRoleFactory;
import za.ac.cput.campusfacilitybooking.repository.LoginRepository;
import za.ac.cput.campusfacilitybooking.repository.RegisterRepository;
import za.ac.cput.campusfacilitybooking.repository.UserRepository;
import za.ac.cput.campusfacilitybooking.repository.UserRoleRepository;
import za.ac.cput.campusfacilitybooking.service.impl.LoginServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    private LoginRepository loginRepository;
    private RegisterRepository registerRepository;
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private LoginServiceImpl service;

    @BeforeEach
    void setUp() {
        loginRepository = Mockito.mock(LoginRepository.class);
        registerRepository = Mockito.mock(RegisterRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        userRoleRepository = Mockito.mock(UserRoleRepository.class);
        service = new LoginServiceImpl(loginRepository, registerRepository, userRepository, userRoleRepository);
    }

    @Test
    void testCreate() {
        Login login = LoginFactory.createLogin("L001", "R001", "admin", "password123");
        when(loginRepository.save(login)).thenReturn(login);

        Login created = service.create(login);

        assertNotNull(created);
        assertEquals(login.getLoginId(), created.getLoginId());
    }

    @Test
    void testRead() {
        Login login = LoginFactory.createLogin("L001", "R001", "admin", "password123");
        when(loginRepository.findById(login.getLoginId())).thenReturn(Optional.of(login));

        Login found = service.read(login.getLoginId());

        assertNotNull(found);
        assertEquals(login.getLoginId(), found.getLoginId());
    }

    @Test
    void testUpdate() {
        Login login = LoginFactory.createLogin("L001", "R001", "admin_updated", "newpassword123");
        when(loginRepository.save(login)).thenReturn(login);

        Login updated = service.update(login);

        assertNotNull(updated);
        assertEquals("admin_updated", updated.getUsername());
    }

    @Test
    void testDelete() {
        String id = "L001";
        when(loginRepository.existsById(id)).thenReturn(true);

        boolean deleted = service.delete(id);

        verify(loginRepository).deleteById(id);
        assertTrue(deleted);
    }

    @Test
    void testAuthenticate_Success() {
        String userId = "U001";
        String registrarId = "R001";
        String username = "admin";
        String password = "password123";
        String email = "admin@cput.ac.za";

        Login login = LoginFactory.createLogin("L001", registrarId, username, password);
        Register register = RegisterFactory.createRegister(registrarId, email, LocalDate.now());
        User user = UserFactory.createUser(userId, "Admin", "User", email, LocalDate.of(1990, 1, 1), "D001");
        UserRole userRole = UserRoleFactory.createUserRole("UR001", userId, Role.ADMIN);

        when(loginRepository.findByUsername(username)).thenReturn(Optional.of(login));
        when(registerRepository.findById(registrarId)).thenReturn(Optional.of(register));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRoleRepository.findByUserIdAndRole(userId, null)).thenReturn(Optional.of(userRole));

        AuthResponse response = service.authenticate(username, password);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals(username, response.getUsername());
        assertEquals(email, response.getEmail());
        assertEquals("ADMIN", response.getRole());
    }

    @Test
    void testAuthenticate_InvalidUsername() {
        when(loginRepository.findByUsername("invalid")).thenReturn(Optional.empty());

        AuthResponse response = service.authenticate("invalid", "password123");

        assertNull(response);
    }

    @Test
    void testAuthenticate_InvalidPassword() {
        Login login = LoginFactory.createLogin("L001", "R001", "admin", "password123");
        when(loginRepository.findByUsername("admin")).thenReturn(Optional.of(login));

        AuthResponse response = service.authenticate("admin", "wrongpassword");

        assertNull(response);
    }

    @Test
    void testAuthenticate_RegisterNotFound() {
        Login login = LoginFactory.createLogin("L001", "R001", "admin", "password123");
        when(loginRepository.findByUsername("admin")).thenReturn(Optional.of(login));
        when(registerRepository.findById("R001")).thenReturn(Optional.empty());

        AuthResponse response = service.authenticate("admin", "password123");

        assertNull(response);
    }

    @Test
    void testAuthenticate_UserNotFound() {
        Login login = LoginFactory.createLogin("L001", "R001", "admin", "password123");
        Register register = RegisterFactory.createRegister("R001", "admin@cput.ac.za", LocalDate.now());

        when(loginRepository.findByUsername("admin")).thenReturn(Optional.of(login));
        when(registerRepository.findById("R001")).thenReturn(Optional.of(register));
        when(userRepository.findByEmail("admin@cput.ac.za")).thenReturn(Optional.empty());

        AuthResponse response = service.authenticate("admin", "password123");

        assertNull(response);
    }
}