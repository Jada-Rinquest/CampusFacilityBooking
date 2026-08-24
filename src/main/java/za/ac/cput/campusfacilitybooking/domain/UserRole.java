package za.ac.cput.campusfacilitybooking.domain;

import jakarta.persistence.*;
import za.ac.cput.campusfacilitybooking.domain.enums.Role;

@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    private String userRoleId;
    private String userId;

    @Enumerated(EnumType.STRING)
    private Role role;

    protected UserRole() {
    }

    public UserRole(String userRoleId, String userId, Role role) {
        this.userRoleId = userRoleId;
        this.userId = userId;
        this.role = role;
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public String getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userRoleId='" + userRoleId + '\'' +
                ", userId='" + userId + '\'' +
                ", role=" + role +
                '}';
    }
}