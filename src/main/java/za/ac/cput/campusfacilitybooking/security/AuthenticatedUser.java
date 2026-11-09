package za.ac.cput.campusfacilitybooking.security;

import java.io.Serializable;
import java.security.Principal;

import za.ac.cput.campusfacilitybooking.dto.AuthResponse;

/*
 * The signed-in user kept in the HTTP session by Spring Security.
 *
 * getName() returns the userId, so in @PreAuthorize expressions
 * "authentication.name" is the id of the person making the request.
 * No password or hash is ever stored here.
 */
public final class AuthenticatedUser implements Principal, Serializable {

    private final String userId;
    private final String username;
    private final String email;
    private final String role;
    private final String accountType;
    private final String firstName;
    private final String lastName;

    public AuthenticatedUser(AuthResponse details) {
        this.userId = details.getUserId();
        this.username = details.getUsername();
        this.email = details.getEmail();
        this.role = details.getRole();
        this.accountType = details.getAccountType();
        this.firstName = details.getFirstName();
        this.lastName = details.getLastName();
    }

    @Override
    public String getName() {
        return userId;
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getAccountType() { return accountType; }

    public boolean isStaff() {
        return "LECTURER".equals(role) || "ADMIN".equals(role);
    }

    public AuthResponse toResponse() {
        AuthResponse response = new AuthResponse(userId, username, email, role, firstName, lastName);
        response.setAccountType(accountType);
        return response;
    }

    @Override
    public String toString() {
        return "AuthenticatedUser{userId='" + userId + "', role='" + role + "'}";
    }
}
