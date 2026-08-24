package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.Register;

import java.time.LocalDate;

public class RegisterFactory {

    public static Register createRegister(String registrarId,
                                          String email,
                                          LocalDate dateRegistered) {

        if (registrarId == null || registrarId.isEmpty()) {
            throw new IllegalArgumentException("Registrar ID is required");
        }

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (dateRegistered == null) {
            throw new IllegalArgumentException("Date registered is required");
        }

        return new Register(
                registrarId,
                email,
                dateRegistered
        );
    }
}
