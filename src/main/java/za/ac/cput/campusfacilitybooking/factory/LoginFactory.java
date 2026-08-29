package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Login;

public class LoginFactory {

    public static Login createLogin(String loginId,
                                    String registrarId,
                                    String username,
                                    String password) {

        if (loginId == null || loginId.isEmpty()) {
            throw new IllegalArgumentException("Login ID is required");
        }

        if (registrarId == null || registrarId.isEmpty()) {
            throw new IllegalArgumentException("Registrar ID is required");
        }

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        return new Login(
                loginId,
                registrarId,
                username,
                password
        );
    }
}
