package za.ac.cput.campusfacilitybooking.service.impl;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import za.ac.cput.campusfacilitybooking.domain.Login;
import za.ac.cput.campusfacilitybooking.domain.Register;
import za.ac.cput.campusfacilitybooking.domain.User;
import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.domain.enums.AccountType;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;
import za.ac.cput.campusfacilitybooking.dto.AuthResponse;
import za.ac.cput.campusfacilitybooking.repository.LoginRepository;
import za.ac.cput.campusfacilitybooking.repository.RegisterRepository;
import za.ac.cput.campusfacilitybooking.repository.UserRepository;
import za.ac.cput.campusfacilitybooking.repository.UserRoleRepository;
import za.ac.cput.campusfacilitybooking.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginRepository loginRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // Shape of a BCrypt hash, e.g. $2a$10$<53 characters>
    private static final Pattern BCRYPT_HASH =
            Pattern.compile("^\\$2[aby]?\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    public LoginServiceImpl(LoginRepository loginRepository,
                            RegisterRepository registerRepository,
                            UserRepository userRepository,
                            UserRoleRepository userRoleRepository,
                            PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    static boolean isHashed(String password) {
        return password != null && BCRYPT_HASH.matcher(password).matches();
    }

    private String hashIfNeeded(String password) {
        return isHashed(password) ? password : passwordEncoder.encode(password);
    }

    private static Login withPassword(Login login, String password) {
        return new Login.Builder()
                .setLoginId(login.getLoginId())
                .setRegistrarId(login.getRegistrarId())
                .setUsername(login.getUsername())
                .setPassword(password)
                .build();
    }

    @Override
    public Login create(Login login) {
        if (login.getPassword() == null || login.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        return loginRepository.save(withPassword(login, hashIfNeeded(login.getPassword())));
    }

    @Override
    public Login read(String id) {
        Optional<Login> login = loginRepository.findById(id);
        return login.orElse(null);
    }

    @Override
    public Login update(Login login) {
        String password = login.getPassword();

        // Passwords are never sent back to clients, so an update without
        // one keeps the stored hash instead of wiping it.
        if (password == null || password.isBlank()) {
            password = loginRepository.findById(login.getLoginId())
                    .map(Login::getPassword)
                    .orElseThrow(() -> new IllegalArgumentException("Password is required"));
        }
        return loginRepository.save(withPassword(login, hashIfNeeded(password)));
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

        if (username == null || password == null) {
            return null;
        }

        // 1. Find login by username
        Optional<Login> loginOpt = loginRepository.findByUsername(username);
        if (loginOpt.isEmpty()) {
            return null;
        }

        Login login = loginOpt.get();

        // 2. Check the password against the stored BCrypt hash
        if (!isHashed(login.getPassword()) || !passwordEncoder.matches(password, login.getPassword())) {
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

        // 5. Account type comes from the email domain (@mycput = student, @cput = staff).
        //    Anything unrecognised gets the least privilege.
        AccountType accountType = AccountType.fromEmail(user.getEmail());
        if (accountType == null) {
            accountType = AccountType.STUDENT;
        }

        // 6. Get UserRole (highest privilege wins: STUDENT < LECTURER < ADMIN).
        //    Students are always STUDENT; staff are at least LECTURER.
        Role storedRole = userRoleRepository.findByUserId(user.getUserId())
                .stream()
                .map(UserRole::getRole)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);

        String role;
        if (accountType == AccountType.STUDENT) {
            role = Role.STUDENT.name();
        } else if (storedRole == Role.ADMIN) {
            role = Role.ADMIN.name();
        } else {
            role = Role.LECTURER.name();
        }

        // 7. Return AuthResponse
        AuthResponse response = new AuthResponse(
                user.getUserId(),
                username,
                user.getEmail(),
                role,
                user.getFirstName(),
                user.getLastName()
        );
        response.setAccountType(accountType.name());

        return response;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return loginRepository.existsByUsername(username);
    }

    @Override
    public int upgradePlaintextPasswords() {
        int upgraded = 0;
        for (Login login : loginRepository.findAll()) {
            String password = login.getPassword();
            if (password != null && !password.isBlank() && !isHashed(password)) {
                loginRepository.save(withPassword(login, passwordEncoder.encode(password)));
                upgraded++;
            }
        }
        return upgraded;
    }

    @Override
    public java.util.List<Login> getAll() {
        return loginRepository.findAll();
    }
}