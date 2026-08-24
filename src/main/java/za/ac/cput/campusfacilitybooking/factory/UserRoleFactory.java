package za.ac.cput.campusfacilitybooking.factory;

import za.ac.cput.campusfacilitybooking.domain.UserRole;

public class UserRoleFactory {

    public static UserRole createUserRole(String userRoleId,
                                          String userId,
                                          String roleId) {

        if (userRoleId == null || userRoleId.isEmpty()) {
            throw new IllegalArgumentException("User Role ID is required");
        }

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (roleId == null || roleId.isEmpty()) {
            throw new IllegalArgumentException("Role ID is required");
        }

        return new UserRole(
                userRoleId,
                userId,
                roleId
        );
    }
}
