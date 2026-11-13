package za.ac.cput.campusfacilitybooking.service;

import za.ac.cput.campusfacilitybooking.controller.RegistrationRequest;
import za.ac.cput.campusfacilitybooking.dto.AuthResponse;

public interface RegistrationService {

    /**
     * Creates a complete account (User, Register, Login, UserRole and
     * Student or Staff) in one transaction.
     *
     * @throws IllegalArgumentException if the details are invalid
     * @throws IllegalStateException    if the username, email or student number is already used
     */
    AuthResponse register(RegistrationRequest request);
}
