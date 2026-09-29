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

    private UserRole(Builder builder) {
        this.userRoleId = builder.userRoleId;
        this.userId = builder.userId;
        this.role = builder.role;
    }

    public static class Builder {
        private String userRoleId;
        private String userId;
        private Role role;

        public Builder setUserRoleId(String userRoleId) {
            this.userRoleId = userRoleId;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public UserRole build() {
            return new UserRole(this);
        }
    }
}