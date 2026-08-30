package za.ac.cput.campusfacilitybooking.service.impl;

import org.springframework.stereotype.Service;
import za.ac.cput.campusfacilitybooking.domain.Login;
import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.dto.AuthResponse;
import za.ac.cput.campusfacilitybooking.repository.LoginRepository;
import za.ac.cput.campusfacilitybooking.repository.RegisterRepository;
import za.ac.cput.campusfacilitybooking.repository.UserRepository;
import za.ac.cput.campusfacilitybooking.repository.UserRoleRepository;
import za.ac.cput.campusfacilitybooking.service.LoginService;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public LoginServiceImpl(LoginRepository loginRepository,
                            RegisterRepository registerRepository,
                            UserRepository userRepository,
                            UserRoleRepository userRoleRepository) {
        this.loginRepository = loginRepository;
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Login create(Login login) {
        return loginRepository.save(login);
    }

    @Override
    public Login read(String id) {
        Optional<Login> login = loginRepository.findById(id);
        return login.orElse(null);
    }

    @Override
    public Login update(Login login) {
        return loginRepository.save(login);
    }

    @Override
    public boolean delete(String id) {
        if (loginRepository.existsById(id)) {
            loginRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public AuthResponse authenticate(String username, String password) {
        // 1. Find login by username
        Login login = loginRepository.findByUsername(username).orElse(null);
        if (login == null) {
            return null;
        }

        // 2. Check password
        if (!login.getPassword().equals(password)) {
            return null;
        }

        // 3. Get Register record
        String registrarId = login.getRegistrarId();
        Register register = registerRepository.findById(registrarId).orElse(null);
        if (register == null) {
            return null;
        }

        // 4. Find User by email
        User user = userRepository.findByEmail(register.getEmail()).orElse(null);
        if (user == null) {
            return null;
        }

        // 5. Get UserRole
        String role = "STUDENT";
        try {
            Optional<UserRole> userRole = userRoleRepository.findByUserIdAndRole(user.getUserId(), null);
            if (userRole.isPresent()) {
                role = userRole.get().getRole().toString();
            }
        } catch (Exception e) {
            // Role not found, use default
        }

        // 6. Return AuthResponse
        return new AuthResponse(
                user.getUserId(),
                username,
                user.getEmail(),
                role,
                user.getFirstName(),
                user.getLastName()
        );
    }
}