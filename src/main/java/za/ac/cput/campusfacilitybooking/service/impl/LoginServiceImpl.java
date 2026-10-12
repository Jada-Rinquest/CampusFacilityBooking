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
        Optional<Login> loginOpt = loginRepository.findByUsername(username);
        if (loginOpt.isEmpty()) {
            return null;
        }

        Login login = loginOpt.get();

        // 2. Check password
        if (!login.getPassword().equals(password)) {
            return null;
        }

        // 3. Get Register record
        String registrarId = login.getRegistrarId();

        Optional<Register> registerOpt = registerRepository.findById(registrarId);
        if (registerOpt.isEmpty()) {
            return null;
        }

        Register register = registerOpt.get();

        // 4. Find User by email
        Optional<User> userOpt = userRepository.findByEmail(register.getEmail());
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();

        // 5. Get UserRole
        String role = "STUDENT";
        try {
            Optional<UserRole> userRoleOpt = userRoleRepository.findByUserIdAndRole(user.getUserId(), null);
            if (userRoleOpt.isPresent()) {
                role = userRoleOpt.get().getRole().toString();
            } else {
            }
        } catch (Exception e) {
        }

        // 6. Return AuthResponse
        AuthResponse response = new AuthResponse(
                user.getUserId(),
                username,
                user.getEmail(),
                role,
                user.getFirstName(),
                user.getLastName()
        );


        return response;
    }

    @Override
    public java.util.List<Login> getAll() {
        return loginRepository.findAll();
    }
}