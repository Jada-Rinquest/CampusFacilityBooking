package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.User;

import java.time.LocalDate;

public class UserFactory {

    public static User createUser(String userId,
                                  String firstName,
                                  String lastName,
                                  String email,
                                  LocalDate dateOfBirth,
                                  String departmentId) {

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        if (departmentId == null || departmentId.isEmpty()) {
            throw new IllegalArgumentException("Department ID is required");
        }

        return new User(
                userId,
                firstName,
                lastName,
                email,
                dateOfBirth,
                departmentId
        );
    }
}
