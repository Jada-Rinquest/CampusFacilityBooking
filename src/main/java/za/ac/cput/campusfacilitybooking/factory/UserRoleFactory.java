package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.UserRole;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;

public class UserRoleFactory {

    public static UserRole createUserRole(String userRoleId,
                                          String userId,
                                          Role role) {

        if (userRoleId == null || userRoleId.isEmpty()) {
            throw new IllegalArgumentException("User Role ID is required");
        }

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }

        return new UserRole(
                userRoleId,
                userId,
                role
        );
    }
}