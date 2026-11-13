package za.ac.cput.campusfacilitybooking.service;

import za.ac.cput.campusfacilitybooking.domain.Login;
import za.ac.cput.campusfacilitybooking.dto.AuthResponse;

public interface LoginService extends IService<Login, String> {
    Login create(Login login);
    Login read(String loginId);
    Login update(Login login);
    boolean delete(String loginId);
    AuthResponse authenticate(String username, String password);

    boolean isUsernameTaken(String username);

    /** Re-saves any login whose password is still plain text as a BCrypt hash. Returns how many changed. */
    int upgradePlaintextPasswords();
}